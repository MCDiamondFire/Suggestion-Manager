const { welcomeChannel } = require("../config");

module.exports = async member => {
    const welcomeMsg = (await member.client.channels.get(welcomeChannel).messages.fetch({ limit: 5 })).first();
    member.send(welcomeMsg.content).catch(() => null);
}
