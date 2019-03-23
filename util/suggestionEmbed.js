var Discord = require('discord.js');
var { serverID } = require('../config.json');

/**
* Return suggestion embed
* @param {Object} message Discord Message
* @param {Array} votes Array of UpVotes and DownVotes
*/
module.exports = (message, votes) => {
	if (message.member == null) return;
	console.log(votes, message.member.nickname, message.member.displayName);
	var embed = new Discord.RichEmbed()
		.setAuthor(message.member.displayName, message.author.avatarURL)
		.setDescription(
			`[:incoming_envelope: Jump to suggestion](https://discordapp.com/channels/${serverID}/${message.channel
				.id}/${message.id})\n\n${message.content}`
		)
		.setColor(getColor(votes))
		.setFooter(`${votes[0] - votes[1]} Upvote${votes[0] - votes[1] == 1 ? '' : 's'} (${votes[0]} | ${votes[1]})`);

	if (message.attachments.array().length > 0) {
		embed.setImage(message.attachments.first().url);
	}

	return embed;
};

function getColor(votes) {
	var ratio = votes[0] - votes[1];

	switch (true) {
		case ratio < 22:
			return '#45B8FF';
		case ratio < 26:
			return '#84E1FB';
		case ratio < 30:
			return '#FFD655';
		case ratio >= 30:
			return '#FF8200';
	}
}
