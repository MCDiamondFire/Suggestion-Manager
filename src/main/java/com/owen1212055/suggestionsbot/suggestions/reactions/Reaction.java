package com.owen1212055.suggestionsbot.suggestions.reactions;

import com.owen1212055.suggestionsbot.instance.BotInstance;
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
