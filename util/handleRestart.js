var suggestionEmbed = require('../util/suggestionEmbed')
var fs = require('fs')
var {serverID, suggestionChannels, votesNeeded, suggestionChannelBot} = require('../config.json')


module.exports = async (client) => {
  var handledMessageIDs = JSON.parse(await fs.readFileSync('./handledSuggestions.json', 'utf-8'))
  
  handledMessageIDs.map(async suggestion => {
    var message = await client.guilds.get(serverID).channels.get(suggestion.channel).fetchMessage(suggestion.message)
    var botMessage = await client.guilds.get(serverID).channels.get(suggestionChannelBot).fetchMessage(suggestion.response)

    var upVotes = message.reactions.find(reaction => reaction.emoji.id == "528944776867741716"),
      downVotes = message.reactions.find(reaction => reaction.emoji.id == "528948590739980289")
      upVotes = upVotes != undefined ? upVotes.count : 0
      downVotes = downVotes != undefined ? downVotes.count : 0

    botMessage.edit(suggestionEmbed(message, [upVotes, downVotes]))
  })

  Promise.all(suggestionChannels.map(async id => {
    var messages = await client.guilds.get(serverID).channels.get(id).fetchMessages({limit: 100})

    //* Add reactions if missing
    messages.map(async msg => {
      await msg.react("528944776867741716")
      await msg.react("528948590739980289")
    })

    messages = messages.filter(msg => handledMessageIDs.find(suggestion => suggestion.message == msg.id) == undefined)
  
    return Promise.all(messages.map(async msg => {
      var upVotes = msg.reactions.find(reaction => reaction.emoji.id == "528944776867741716"),
      downVotes = msg.reactions.find(reaction => reaction.emoji.id == "528948590739980289")
      upVotes = upVotes != undefined ? upVotes.count : 0
      downVotes = downVotes != undefined ? downVotes.count : 0
  
      if(upVotes - downVotes >= votesNeeded) {
        var botResponseID = (await client.guilds.get(serverID).channels.get(suggestionChannelBot).send(suggestionEmbed(msg, [upVotes, downVotes]))).id
        handledMessageIDs.push({"message": msg.id, "channel": msg.channel.id, "response": botResponseID})
      }
    }))
  }))
  .then(results => {
    fs.writeFileSync('./handledSuggestions.json', JSON.stringify(handledMessageIDs), 'utf-8')
  })

}