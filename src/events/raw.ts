import * as Discord from "discord.js";
import { client } from "..";
import { MongoClient } from "../database/client";
const {
    required,
    emojis,
    popularSuggestions,
    feed
  } = require("../config.json"),
  messageData = require("../reactions.json"),
  suggestions = MongoClient.db("SuggestionManager").collection("Suggestions"),
  handledSuggestions = MongoClient.db("SuggestionManager").collection(
    "HandledSuggestions"
  );

module.exports = async (packet: any) => {
  if (packet.t !== "MESSAGE_REACTION_ADD") return;
  //* Return if bot
  if (packet.d.user_id === client.user.id) return;

  const channel = client.channels.get(
    packet.d.channel_id
  ) as Discord.TextChannel;
  const message = (await channel.messages.fetch(
    packet.d.message_id
    // @ts-ignore
  )) as Discord.Message;

  //* Remove reaction if from message author
  // if (message.author.id === packet.d.user_id) {
  //   message.reactions.get(packet.d.emoji.id).users.remove(packet.d.user_id);
  //   return;
  // }

  const notifChannel = client.channels.get(feed) as Discord.TextChannel;

  //* Send messages upon reactions
  const messages = messageData[packet.d.emoji.id];
  if (messages) {
    notifChannel.send(
      messages.publicMessage
        .replace(/{{user}}/g, message.member.displayName)
        .replace(
          /{{reacter}}/g,
          message.guild.member(packet.d.user_id).displayName
        )
        .replace(/{{link}}/g, message.url)
        .replace(/{{emoji}}/g, `<:${packet.d.emoji.name}:${packet.d.emoji.id}>`)
    );

    message.author.send({
      embed: JSON.parse(
        JSON.stringify(messages.embed)
          .replace(/{{user}}/g, message.member.displayName)
          .replace(
            /{{reacter}}/g,
            message.guild.member(packet.d.user_id).displayName
          )
          .replace(
            /{{emoji}}/g,
            `<:${packet.d.emoji.name}:${packet.d.emoji.id}>`
          )
          .replace(/{{reacter_mention}}/g, `<@${packet.d.user_id}>`)
      )
    });
  }

  let upvote;
  if (packet)
    upvote = message.reactions.get(emojis.upvote) as Discord.MessageReaction;
  const downvote = message.reactions.get(
    emojis.downvote
  ) as Discord.MessageReaction;

  if (
    upvote.count - 1 - (downvote.count - 1) >= required &&
    !(await handledSuggestions.findOne({ origMessage: message.id }))
  ) {
    (client.channels.get(popularSuggestions) as Discord.TextChannel)
      .send(
        new Discord.MessageEmbed()
          .setColor(0x32ffaa)
          .setDescription(`📨 [Jump to suggestion](${message.url})`)
          .addField(
            "\u200b",
            message.content.length > 256
              ? message.content.substring(0, 256) + "..."
              : message.content
          )
          .setAuthor(
            message.member.displayName,
            message.author.avatarURL({ size: 128 })
          )
          .setFooter("Posted")
          .setTimestamp(message.createdTimestamp)
      )
      .then(msg => {
        handledSuggestions.insertOne({ id: msg.id, origMessage: message.id });
      });
  }
};
