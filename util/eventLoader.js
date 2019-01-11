
const reqEvent = (event) => require(`../events/${event}`)

module.exports = (client) => {
  client.on('ready', () => reqEvent('ready')(client));
  client.on('messageReactionAdd', reqEvent('messageReactionAdd'))
  client.on('message', reqEvent('message'));
};