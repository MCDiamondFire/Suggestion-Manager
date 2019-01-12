var fs = require('fs')
var suggestionEmbed = require('../util/suggestionEmbed')
var debug = require('../util/debug')

var {suggestionChannels, votesNeeded, suggestionChannelBot} = require('../config.json')

module.exports = async (reaction, user) => {
  //* Check if channel ID = one of the suggestion channels
  if(suggestionChannels.includes(reaction.message.channel.id)) {
    //* Disallow users reacting to their own suggestion
    if(reaction.message.author.id == user.id && user.id != "531254995639861260") {
      debug("info", `${user.tag} reacted to their own suggestion on ${reaction.message.id}`)
      reaction.remove(user) 
      return
    }

    if(user.id != "531254995639861260") debug("info", `${user.tag} reacted with ${reaction.emoji.name} on ${reaction.message.id}`)
    //* Check if message is already handled
    var handledMessageIDs = JSON.parse(await fs.readFileSync('./handledSuggestions.json', 'utf8'))
    //* Get its upVotes & downVotes
    var upVotes = await (reaction.message.reactions.find(reaction => reaction.emoji.id == "528944776867741716")) ? reaction.message.reactions.find(reaction => reaction.emoji.id == "528944776867741716").count : 0
    var downVotes = await (reaction.message.reactions.find(reaction => reaction.emoji.id == "528948590739980289")) ? reaction.message.reactions.find(reaction => reaction.emoji.id == "528948590739980289").count : 0

    //* Check if vote count is >= with the one in config
    if(upVotes - downVotes >= votesNeeded) {
      //* edit message if already exists
      if(!handledMessageIDs.find(suggestion => suggestion.message == reaction.message.id)) {
        var botResponse = (await reaction.message.guild.channels.get(suggestionChannelBot).send(suggestionEmbed(reaction.message, [upVotes, downVotes]))).id
        
        //* Push it to the handledMessages & save to file
        handledMessageIDs.push({message: reaction.message.id, "channel": reaction.message.channel.id, response: botResponse})
        fs.writeFileSync("./handledSuggestions.json", JSON.stringify(handledMessageIDs))
      } else {
        //* Find suggestion
        var suggestion = handledMessageIDs.find(suggestion => suggestion.message == reaction.message.id)

        //* Update it
        var botSuggestion = await reaction.message.guild.channels.get(suggestionChannelBot).fetchMessage(suggestion.response)
        botSuggestion.edit(suggestionEmbed(reaction.message, [upVotes, downVotes]))
      }
    }
  }
}