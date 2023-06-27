package com.diamondfire.suggestionsbot.sys.message.acceptors;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.bot.discord.events.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

public class CommandAcceptor implements MessageAcceptor {
    
    @Override
    public boolean accept(Message message) {
        if (message.getContentDisplay().startsWith(DiscordInstance.getConfig().getPrefix()) && !message.getAuthor().isBot()) {
            DiscordInstance.getHandler().run(new CommandEvent(message));
            return true;
        }
        
        return false;
    }
}
