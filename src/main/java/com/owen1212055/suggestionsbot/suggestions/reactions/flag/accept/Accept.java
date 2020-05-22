package com.owen1212055.suggestionsbot.suggestions.reactions.flag.accept;

import com.owen1212055.suggestionsbot.suggestions.reactions.ReactionPriority;
import com.owen1212055.suggestionsbot.suggestions.reactions.flag.ReactionFlag;

import java.awt.*;

public class Accept extends ReactionFlag {
    @Override
    public Color getColor() {
        return Color.decode("#4CCD6A");
    }

    @Override
    public String getEmbedName() {
        return "Accepted";
    }

    @Override
    public boolean preventPopular() {
        return true;
    }

    @Override
    public int getPriority() {
        return ReactionPriority.HIGHEST;
    }

    @Override
    public long getID() {
        return 612434584109580328L;
    }

    @Override
    public String getIdentifier() {
        return "accepted";
    }
}
