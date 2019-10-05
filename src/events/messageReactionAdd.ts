import * as D from "discord.js";
import { client } from "..";

import { MongoClient } from "../database/client";
const suggestions = MongoClient.db("SuggestionManager").collection("Suggestions");
const handledSuggestions = MongoClient.db("SuggestionManager").collection("HandledSuggestions");

const reactions = require("../reactions.json");
const conf = require("../config.json");

module.exports = async (reaction:D.MessageReaction, user:D.User) => {
    // ? Resolve partial message
    if (reaction.message.partial) await reaction.message.fetch();
    const { message } = reaction;

    // ? Ignore DMs, unavailable guild, bots
    if (!message.guild || !message.guild.available || user.bot) return;

    // ? Mirror reactions in #popular-suggestions
    if (message.channel.id === conf.popularSuggestions && message.author.id === client.user.id && message.embeds[0]) {
        // Parse discord link from description
        const data = message.embeds[0].description.split("https://discordapp.com/")[1].replace(/\)/g, "").split("/").slice(1);
        // Get channel
        const channel = client.channels.get(data[1]) as D.TextChannel;
        // Verify channel
        if (!channel) return;
        // Fetch message
        const msg = await channel.messages.fetch(data[2]);
        // Verify message & mirror
        if (msg) msg.react(reaction.emoji.id || reaction.emoji.name).catch(() => null);

        return;
    }

    // ? Ignore non suggestion channels
    if (!conf.suggestionChannels.includes(reaction.message.channel.id)) return;

    // ? Prevent self reacting
    // if (message.author.id === user.id) return reaction.users.remove(user);

    // ? Channels
    const reactedFeed = client.channels.get(conf.reactedFeed) as D.TextChannel;
    const feed = client.channels.get(conf.feed) as D.TextChannel;
    const popular = client.channels.get(conf.popularSuggestions) as D.TextChannel;
    
  // ? Find & verify +/- reactions
    const upvoteR = message.reactions.get(conf.emojis.upvote),
          downvoteR = message.reactions.get(conf.emojis.downvote);
    if (!upvoteR || !downvoteR) return;

    // ? Parse counts & net votes
    const upvotes = upvoteR.count,
          downvotes = downvoteR.count,
          net = upvotes - downvotes;


    // ? Log handled suggestions
    if ([conf.emojis.accepted, conf.emojis.denied].includes(reaction.emoji.id)) {
        // Parse handle "mode"
        const mode = reaction.emoji.id === conf.emojis.accepted ? "Accepted" : "Denied";

        // Send the message
        reactedFeed.send(new D.MessageEmbed()
            .setAuthor(user.tag, user.avatarURL({ size: 128 }))
            .setTitle(`${reaction.emoji.toString()} | Suggestion ${mode}`)
            .setColor(mode === "Accepted" ? 5046122 : 16733011)
            .setDescription(`[Suggestion](${message.url}) posted by ${message.author.toString()} was ${mode.toLowerCase()} by ${user.toString()}.`)
            .addField("\u200b", message.content.substring(0, 196) + (message.content.length > 196 ? "..." : ""))
            .addField("» Net Votes", net, true)
            .addField("» Popularity", Math.round((upvotes / (upvotes + downvotes)) * 100) + "%", true)
            .setFooter("Posted")
            .setTimestamp(message.createdTimestamp));
    }

    // ? Find reactions and send corresponding messages
    const messageData = reactions[reaction.emoji.id];
    if (messageData) {
        // Replace templates with corresponding values
        const feedMsg = messageData.publicMessage
            .replace(/{{emoji}}/g, reaction.emoji.toString())
            .replace(/{{user}}/g, `${message.member.displayName} (${message.author.tag})`)
            .replace(/{{reacter}}/g, `${message.guild.member(user.id).displayName} (${user.tag})`)
            .replace(/{{user_mention}}/g, message.author.toString())
            .replace(/{{reacter_mention}}/g, user.toString());

        // Send to #discussion
        feed.send(feedMsg);

        // Replace templates with corresponding values
        const dmMsg = JSON.parse(JSON.stringify(messageData.embed)
            .replace(/{{emoji}}/g, reaction.emoji.toString())
            .replace(/{{user}}/g, `${message.member.displayName} (${message.author.tag})`)
            .replace(/{{reacter}}/g, `${message.guild.member(user.id).displayName} (${user.tag})`)
            .replace(/{{user_mention}}/g, message.author.toString())
            .replace(/{{reacter_mention}}/g, user.toString()));

        // Send to author in DM
        message.author.send({ embed: dmMsg }).catch(() => null);
    }

    // ? Find popular suggestion DB entry
    const handledData = await handledSuggestions.findOne({ origMessage: message.id });
    if (handledData) {
        const msg = await message.channel.messages.fetch(handledData.origMessage);
        const popMsg = await popular.messages.fetch(handledData.id);

        const embed = new D.MessageEmbed(popMsg.embeds[0])
            .setFooter(`${net} Net Votes (${upvotes - 1} : ${downvotes - 1})`);

        if (embed.footer !== popMsg.embeds[0].footer.text) popMsg.edit(embed);
    }
    
    // ? Handle popular suggestion
    else if (net >= conf.required) {
        // Send author notification in DM
        message.author.send(new D.MessageEmbed()
            .setColor(0x32ffaa)
            .setDescription(`Your [suggestion](${message.url}) made it to ${popular.toString()}!`)
            .setFooter(`${net} Net Votes (${upvotes - 1} : ${downvotes - 1})`)).catch(() => null);

        // Build embed for #popular-suggestions
        const embed = new D.MessageEmbed()
            .setColor(0x32ffaa)
            .attachFiles([new D.MessageAttachment(message.author.avatarURL({ size: 128, format: "gif" }), "icon.gif")])
            .setAuthor(`${message.member.displayName} (${message.author.tag})`, "attachment://icon.gif")
            .setDescription(`📨 [Jump to suggestion](${message.url})`)
            .addField("\u200b", message.content.substring(0, 197) + (message.content.length > 200 ? "..." : ""))
            .setFooter(`${net} Net Votes (${upvotes - 1} : ${downvotes - 1})`)
            .setTimestamp();
        
        // Send embed
        const sent = await popular.send(embed);
        // Insert suggestion into database
        handledSuggestions.insertOne({ origMessage: message.id, id: sent.id });
    }
}