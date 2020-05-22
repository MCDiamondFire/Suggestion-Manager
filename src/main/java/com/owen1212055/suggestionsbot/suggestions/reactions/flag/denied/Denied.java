package com.owen1212055.suggestionsbot.suggestions.reactions.flag.denied;

import com.owen1212055.suggestionsbot.suggestions.reactions.ReactionPriority;
import com.owen1212055.suggestionsbot.suggestions.reactions.flag.ReactionFlag;

import java.awt.*;

public class Denied extends ReactionFlag {
    @Override
    public Color getColor() {
        return Color.decode("#FE5253");
    }

    @Override
    public String getEmbedName() {
        return "Denied";
    }

    @Override
    public boolean preventPopular() {
        return false;
    }

    @Override
    public int getPriority() {
        return ReactionPriority.LOWEST;
    }

    @Override
    public long getID() {
        return 612438944876855349L;
    }

    @Override
    public String getIdentifier() {
        return "denied";
    }
}
