package com.owen1212055.suggestionsbot.suggestions.reactions.flag.denied;

import com.owen1212055.suggestionsbot.suggestions.reactions.ReactionPriority;
import com.owen1212055.suggestionsbot.suggestions.reactions.flag.ReactionFlag;

import java.awt.*;

public class Duplicate extends ReactionFlag {
    @Override
    public Color getColor() {
        return Color.decode("#FF7042");
    }

    @Override
    public String getEmbedName() {
        return "Marked as a Duplicate";
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
        return 612445225134194699L;
    }

    @Override
    public String getIdentifier() {
        return "dupe";
    }
}
