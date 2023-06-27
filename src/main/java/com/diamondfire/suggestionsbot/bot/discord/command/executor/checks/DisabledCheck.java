package com.diamondfire.suggestionsbot.bot.discord.command.executor.checks;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.bot.discord.command.reply.PresetBuilder;
import com.diamondfire.suggestionsbot.bot.discord.command.reply.feature.informative.*;
import com.diamondfire.suggestionsbot.bot.discord.events.CommandEvent;

public class DisabledCheck implements CommandCheck {
    
    @Override
    public boolean check(CommandEvent event) {
        return !DiscordInstance.getHandler().getDisabledHandler().isDisabled(event.getCommand());
    }
    
    @Override
    public void buildMessage(CommandEvent event, PresetBuilder builder) {
        builder.withPreset(
                new InformativeReply(InformativeReplyType.ERROR, "Disabled!", "This command has been disabled until further notice.")
        );
        
    }
    
    
}
