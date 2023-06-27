package com.diamondfire.suggestionsbot.bot.discord.command.reply.feature;

import net.dv8tion.jda.api.EmbedBuilder;

public interface ReplyPreset {
    
    void applyFeature(EmbedBuilder builder);
}
