package com.diamondfire.suggestionsbot.suggestions.reactions;

import com.diamondfire.suggestionsbot.instance.BotInstance;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;

import java.awt.*;

public abstract class Reaction {

    public abstract long getID();

    public RichCustomEmoji getEmote() {
        return BotInstance.getJda().getEmojiById(getID());
    }

    public abstract String getIdentifier();

    public abstract Color getColor();

    public abstract int getPriority();


}
