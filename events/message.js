const {suggestionChannels} = require('../config.json');

module.exports = async message => {
  //* Bot == STOP
  if (message.author.bot) return;

  //* Message sent in a suggestion channel
  if(suggestionChannels.includes(message.channel.id)) {
    message.react("528944776867741716")
    .then(() => message.react("528948590739980289"))
  }
}