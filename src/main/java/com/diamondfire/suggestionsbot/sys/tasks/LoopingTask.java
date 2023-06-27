package com.diamondfire.suggestionsbot.sys.tasks;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import net.dv8tion.jda.api.entities.TextChannel;

public interface LoopingTask extends Runnable {
    
    TextChannel RESPONSE_CHANNEL = DiscordInstance.getJda().getTextChannelById(DiscordInstance.LOG_CHANNEL);
    
    long getInitialStart();
    
    long getNextLoop();
    
}
