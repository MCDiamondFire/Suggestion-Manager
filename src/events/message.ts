const { prefix, suggestionChannels, feed, emojis } = require("../config.json");

import * as Discord from "discord.js";
import { MongoClient } from "../database/client";

module.exports = (message: Discord.Message) => {
  //* Prevent bots
  if (message.author && message.author.bot) return;

  //* Message is command
  if (!message.content.startsWith(prefix)) {
    if (!suggestionChannels.includes(message.channel.id)) return;

    const sendTo = message.client.channels.get(feed) as Discord.TextChannel;
    if (!sendTo) return;
    sendTo.send(
      new Discord.MessageEmbed()
        .setColor(0x32ffaa)
        .setDescription(
          `📨 [New ${suggOrIssue(message) ? "suggestion" : "issue"} posted](${
            message.url
          })`
        )
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
        .setFooter(
          `Posted in #${(message.channel as Discord.TextChannel).name}`
        )
        .setTimestamp(message.createdTimestamp)
    );

    if (suggOrIssue(message)) {
      const suggestions = MongoClient.db("SuggestionManager").collection(
        "Suggestions"
      );

      message.react(emojis.upvote).then(() => message.react(emojis.downvote));

      suggestions.insertOne({
        id: message.id
      });
    } else {
      const issues = MongoClient.db("SuggestionManager").collection("Issues");

      issues.insertOne({
        id: message.id
      });
    }

    return;
  }

  const command = message.content.split(" ")[0].slice(prefix.length),
    params = message.content.split(" ").slice(1),
    perms = message.client.elevation(message),
    cmd: any =
      message.client.commands.get(command) ||
      message.client.commands.get(message.client.aliases.get(command));

  //* Run command if found
  if (cmd) {
    //TODO Send fancy no permission message
    if (
      typeof cmd.config.permLevel != "undefined" &&
      perms < cmd.config.permLevel
    )
      return;

    //* Run the command
    cmd.run(message, params, perms);
  }
};

function suggOrIssue(message: Discord.Message) {
  return (message.channel as Discord.TextChannel).name !== "issues";
}
