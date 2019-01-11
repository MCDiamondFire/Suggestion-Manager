var debug = require('../util/debug')
var {votesNeeded} = require('../config.json')

module.exports = async client => {
  debug("success", `Connected to Discord as ${client.user.tag}`)
  //* Set bot status & activity
  client.user.setStatus(process.env.NODE_ENV == "dev" ? "dnd" : "online")
  client.user.setActivity(process.env.NODE_ENV == "dev" ? "Timeraa code..." : `for ${votesNeeded} upvotes`, {
    type: "WATCHING"
  })

  require('../util/handleRestart')(client)
}