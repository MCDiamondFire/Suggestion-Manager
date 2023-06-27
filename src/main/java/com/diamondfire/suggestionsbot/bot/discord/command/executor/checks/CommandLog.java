package com.diamondfire.suggestionsbot.bot.discord.command.executor.checks;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.bot.discord.command.reply.PresetBuilder;
import com.diamondfire.suggestionsbot.bot.discord.events.CommandEvent;
import com.diamondfire.suggestionsbot.sys.database.impl.DatabaseQuery;
import com.diamondfire.suggestionsbot.sys.database.impl.queries.BasicQuery;

public class CommandLog implements CommandCheck {
    
    @Override
    public boolean check(CommandEvent event) {
        if (!DiscordInstance.getConfig().isDevBot()) {
            new Thread(() -> {
                new DatabaseQuery()
                        .query(new BasicQuery("INSERT INTO owen.cmd_log (user, command, alias, channel, time) VALUES (?,?,?,?,CURRENT_TIMESTAMP())", (statement) -> {
                            statement.setLong(1, event.getMember().getIdLong());
                            statement.setString(2, event.getCommand().getName());
                            statement.setString(3, event.getAliasUsed());
                            statement.setLong(4, event.getChannel().getIdLong());
                        })).compile();
                
            }).start();
        }
        return true;
    }
    
    @Override
    public void buildMessage(CommandEvent event, PresetBuilder builder) {
    }
    
    
}
