package com.diamondfire.suggestionsbot.bot.discord.command.reply.feature.informative;

import org.jetbrains.annotations.Nullable;

import java.awt.*;

public enum InformativeReplyType {
    SUCCESS("Success!", Color.GREEN),
    INFO("Notice!", null),
    ERROR("Error!", Color.RED);
    
    private final String title;
    private final Color color;
    
    InformativeReplyType(@Nullable String title, @Nullable Color color) {
        this.title = title;
        this.color = color;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Color getColor() {
        return color;
    }
}
