package com.diamondfire.suggestionsbot.bot.discord.events;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.bot.discord.restart.RestartHandler;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ReadyEvent extends ListenerAdapter {
    
    @Override
    public void onReady(@Nonnull net.dv8tion.jda.api.events.ReadyEvent event) {
        super.onReady(event);
        
        RestartHandler.recover(event.getJDA());
        DiscordInstance.getScheduler().initialize();
    }
}
