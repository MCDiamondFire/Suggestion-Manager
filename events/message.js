var {suggestionChannels} = require('../config.json'),
  Discord = require('discord.js')

module.exports = async message => {
  //* Bot == STOP
  if (message.author.bot) return;

  //* Message sent in a suggestion channel
  if(suggestionChannels.includes(message.channel.id)) {
    message.react("528944776867741716")
    .then(() => message.react("528948590739980289"))
  }

  //* Send Notification of Suggestion/Issue in #general
  if(suggestionChannels.includes(message.channel.id)
  || message.channel.id == "528935415982587904") {
    var embed = new Discord.RichEmbed()
    .setAuthor(message.member.displayName, message.author.avatarURL)
    .setURL(`https://discordapp.com/channels/${message.guild.id}/${message.channel.id}/${message.id}`)
    .setColor("#00FF8C")
    .setDescription(message.cleanContent.length < 60 ? message.cleanContent : message.cleanContent.slice(0, 60) + "...")
    .setTitle(`:incoming_envelope: Jump to ${suggestionChannels.includes(message.channel.id) ? "suggestion" : "issue"}`)
    .setFooter(`Posted in #${message.channel.name}`)
    

    message.guild.channels.get("528932649394241536")
    .send(embed)
  }
}