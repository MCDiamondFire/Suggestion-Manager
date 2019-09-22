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
  if (message.author.id === packet.d.user_id) {
    message.reactions.get(packet.d.emoji.id).users.remove(packet.d.user_id);
    return;
  }

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

  if (packet.d.emoji.id === emojis.possible) {
    notifChannel.send(
      `*${
        message.member.displayName
      }'s suggestion has been marked as possible (<:possible:625114464022429757>) by ${
        message.guild.members.get(packet.d.user_id).displayName
      }.*`
    );
    message.author
      .send({
        embed: new Discord.MessageEmbed()
          .setTitle("<:possible:625114464022429757> | Possible")
          .setDescription(
            `Your [suggestion](${message.url}) has been marked as possible by <@${packet.d.user_id}>, meaning what you suggested is already possible to do in DiamondFire. Please ask them for an explanation as to how if you can't figure it out.`
          )
          .setColor("#ff5353")
      })
      .catch(e => null);
  }

  //* I think we could simplify this with a json file lol
  //* Yeah let me do that lmao

  if (packet.d.emoji.id === emojis.accepted) {
    notifChannel.send(
      `*${
        message.member.displayName
      }'s suggestion has been marked as accepted (<:accepted:625114466534555669>) by ${
        message.guild.members.get(packet.d.user_id).displayName
      }.*`
    );
    message.author.send(
      new Discord.MessageEmbed()
        .setTitle("<accepted:625114466534555669> | Accepted")
        .setColor("#4cff6a")
        .setDescription(
          `Your [suggestion](${message.url}) as been accepted by <@${packet.d.user_id}>! If nothing changes it will be in DiamondFire eventually.`
        )
    );
  }

  if (packet.d.emoji.id === emojis.prioritymax) {
    notifChannel.send(
      `*${
        message.member.displayName
      }'s suggestion has been marked as highest priority (<:prioritymax:625114464395722752>) by ${
        message.guild.members.get(packet.d.user_id).displayName
      }.*`
    );
    message.author.send(
      new Discord.MessageEmbed()
        .setTitle("<:prioritymax:625114464395722752> | Highest Priority")
        .setColor("#ff7043")
        .setDescription(
          `Your [suggestion](${message.url}) has ben marked as the highest priority by <@${packet.d.user_id}>. This means it will likely be added in the next patch, or possibly a bit later. The developers are currently working on many things at once, so be patient!`
        )
    );
  }

  if (packet.d.emoji.id === emojis.prioritymid) {
    notifChannel.send(
      `*${
        message.member.displayName
      }'s suggestion has been marked as highest priority (<:prioritymax:625114464395722752>) by ${
        message.guild.members.get(packet.d.user_id).displayName
      }.*`
    );
    message.author.send(
      new Discord.MessageEmbed()
        .setTitle("<:prioritymax:625114464395722752> | Highest Priority")
        .setDescription(
          `Your [suggestion](${message.url}) has ben marked as the highest priority by <@${packet.d.user_id}>. This means it will likely be added in the next patch, or possibly a bit later. The developers are currently working on many things at once, so be patient!`
        )
    );
  }

  if (packet.d.emoji.id === emojis.prioritymid) {
    notifChannel.send(
      `*${
        message.member.displayName
      }'s suggestion has been marked as highest priority (<:prioritymax:625114464395722752>) by ${
        message.guild.members.get(packet.d.user_id).displayName
      }.*`
    );
    message.author.send(
      new Discord.MessageEmbed()
        .setTitle("<:prioritymax:625114464395722752> | Highest Priority")
        .setDescription(
          `Your [suggestion](${message.url}) has ben marked as the highest priority by <@${packet.d.user_id}>. This means it will likely be added in the next patch, or possibly a bit later. The developers are currently working on many things at once, so be patient!`
        )
    );
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
