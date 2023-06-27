package com.diamondfire.suggestionsbot.bot.discord.command.executor.checks;

import com.diamondfire.suggestionsbot.bot.discord.command.reply.PresetBuilder;
import com.diamondfire.suggestionsbot.bot.discord.events.CommandEvent;

// Command checks are run before a command is executed.
public interface CommandCheck {
    
    boolean check(CommandEvent event);
    
    void buildMessage(CommandEvent event, PresetBuilder builder);
}
