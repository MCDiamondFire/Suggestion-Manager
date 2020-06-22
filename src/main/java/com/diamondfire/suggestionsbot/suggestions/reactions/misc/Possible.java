package com.diamondfire.suggestionsbot.suggestions.reactions.misc;

import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionPriority;

import java.awt.*;

public class Possible extends Reaction {
    @Override
    public long getID() {
        return 612436265530294337L;
    }

    @Override
    public String getIdentifier() {
        return "pos";
    }

    @Override
    public Color getColor() {
        return Color.decode("#FF5353");
    }

    @Override
    public int getPriority() {
        return ReactionPriority.LOWEST;
    }
}
