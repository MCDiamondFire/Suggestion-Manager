package com.diamondfire.suggestionsbot.suggestions.reactions;

import com.diamondfire.suggestionsbot.instance.BotInstance;
import net.dv8tion.jda.api.entities.Emote;

import java.awt.*;

public abstract class Reaction {

    public abstract long getID();

    public Emote getEmote() {
        return BotInstance.getJda().getEmoteById(getID());
    }

    public abstract String getIdentifier();

    public abstract Color getColor();

    public abstract int getPriority();


}
