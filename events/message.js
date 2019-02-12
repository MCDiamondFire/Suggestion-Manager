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
  if((suggestionChannels.includes(message.channel.id)
  || message.channel.id == "528935415982587904") && message.cleanContent.length > 15) {
    var embed = new Discord.RichEmbed()
    .setAuthor(message.member.displayName, message.author.avatarURL)
    .setColor("#00FF8C")
    .setDescription(`[:incoming_envelope: New ${suggestionChannels.includes(message.channel.id) ? "suggestion" : "issue"} posted](https://discordapp.com/channels/${message.guild.id}/${message.channel.id}/${message.id})\n\n${message.cleanContent.length < 120 ? message.cleanContent : message.cleanContent.slice(0, 120) + "..."}`)
    .setFooter(`Posted in #${message.channel.name}`)
    

    message.guild.channels.get("528932649394241536")
    .send(embed)
  }
}