**Bot Specific Information**

Suggestion Manager is a bot that is used in the DiamondFire Suggestions discord. Suggestion Manager helps make handling suggestions easier with reactions, popular suggestions and tools to help with moderation
***

**Contribution** 

This bot is coded in **java 11** and there are no current rules in place for contribution. If you want to add/moidfy something you are able to create a **fork** and submit a **pull request**.

**Config File**

This project uses a config file inorder to specify things such as bot token, prefix and database information. When you
first boot the bot, you will need to create this file.

```json
{
    "TOKEN": "bot token",
        "DB_URL": "jdbc:db_type://ip:port/schema",
        "DB_USER": "dbuser",
        "DB_PASS": "dbpassword",

    "RATIO": 18,
    "GUILDS": [long],

    "UPVOTE": long,
    "DOWNVOTE": long,

    "DISCUSSION": long,
    "REACTION_POPULAR": long,
    "REACTION_LOG": long,

    "CODE_SUGGESTIONS_CHANNEL": long,
    "GENERAL_SUGGESTIONS_CHANNEL": long,
    "ISSUES_CHANNEL": long,
    "BETA_ISSUES_CHANNEL": long
}
```

**Swear Filter**

After setting up the config, you will need to paste the following into the swear filter file. Otherwise, commands will not work and it will error whenever a message is sent.

```json
{
  "equal": [
  ],
  "prefix": [
  ],
  "suffix": [
  ],
  "part": [
  ]
}
```
