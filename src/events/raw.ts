import * as Discord from "discord.js";
import { client } from "..";
import { MongoClient } from "../database/client";
const {
    required,
    emojis,
    popularSuggestions,
    feed,
    reactedFeed
  } = require("../config.json"),
  messageData = require("../reactions.json"),
  suggestions = MongoClient.db("SuggestionManager").collection("Suggestions"),
  handledSuggestions = MongoClient.db("SuggestionManager").collection(
    "HandledSuggestions"
  );

module.exports = async (packet: any) => {
  //* Check for messageReactionAdd
  if (packet && packet.t !== "MESSAGE_REACTION_ADD") return;

  //* Fetch channel & message
  const channel = client.channels.get(
    packet.d.channel_id
  ) as Discord.TextChannel;
  if (!channel) return;
  const message = await channel.messages.fetch(
    packet.d.message_id
  ) as Discord.Message;
  if (!message) return;

  //* Mirror reactions from #popular-suggestions
  if (packet.d.channel_id === popularSuggestions) {
    const data = message.embeds[0].description.split("https://discordapp.com/channels/")[1].replace(/\)/g, "").split("/").slice(1);
    const channel = client.channels.get(data[0]) as Discord.TextChannel;
    const msg = await channel.messages.fetch(data[1]);
    msg.react(packet.d.emoji.id || packet.d.emoji.name).catch(e => null);

    return;
  }

  //* Log handled suggestions
  if ([emojis.accepted, emojis.denied].includes(packet.d.emoji.id)) (client.channels.get(reactedFeed) as Discord.TextChannel).send(
    new Discord.MessageEmbed()
    .setAuthor(message.author.tag, message.author.avatarURL({ size: 128 }))
    .setTitle(`<:${packet.d.emoji.name}:${packet.d.emoji.id}> | Suggestion Handled`)
    .setColor(packet.d.emoji.id === emojis.accepted ? 5046122 : 16733011)
    .setDescription(`[Suggestion](${message.url}) marked as **${packet.d.emoji.id === emojis.accepted ? "accepted" : "denied"}** by <@${packet.d.user_id}>.`)
    .addField("\u200b", message.content.length > 128 ? message.content.substring(0, 125) + "..." : message.content || "*No message*")
    .addField("» Net Votes", message.reactions.get(emojis.upvote).count - message.reactions.get(emojis.downvote).count, true)
    .addField("» Poster", message.author.toString(), true)
    .setTimestamp()
  );

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

  //* Calculate net votes
  const upvote = message.reactions.get(emojis.upvote) as Discord.MessageReaction;
  const downvote = message.reactions.get(emojis.downvote) as Discord.MessageReaction;
  if (!upvote || !downvote) return;
  const net = upvote.count - downvote.count;
  
  //* Check if suggestion should be registered as popular
  if (net >= required && !await handledSuggestions.findOne({ origMessage: message.id })) {
    // Send to discussion
    notifChannel.send(`*${message.member.displayName}'s suggestion has made it to popular suggestions!*`);

    // Send to author
    message.author.send(new Discord.MessageEmbed()
      .setTitle("Suggestion Popular")
      .setDescription(`Your [suggestion](${message.url}) made it into popular suggestions!`)
      .setColor(0x32ffaa));

    // Send to popular suggestions
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
        // Insert into DB
        handledSuggestions.insertOne({ id: msg.id, origMessage: message.id });
      });
  }
};
