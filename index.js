//* Load .env
require('dotenv').load();

var fs = require('fs')
var debug = require('./util/debug')

debug("info", "Running in development mode!")

//* create handledSuggestions.json
if(!fs.existsSync('./handledSuggestions.json'))
  fs.writeFileSync('./handledSuggestions.json', "[]", "utf-8")

//* Require stuff
const Discord = require('discord.js'),
client = new Discord.Client();

//* Load events
require('./util/eventLoader')(client);

//* Login to Discord API
debug("info", "Logging in...")
client.login(process.env.token)
.catch(err => debug("error", `Discord: ${err.message}`))

//* Export client
module.exports.client = client