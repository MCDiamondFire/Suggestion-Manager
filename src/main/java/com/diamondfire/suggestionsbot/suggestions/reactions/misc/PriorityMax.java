package com.diamondfire.suggestionsbot.suggestions.reactions.misc;

import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionPriority;

import java.awt.*;

public class PriorityMax extends Reaction {
    @Override
    public long getID() {
        return 612404632072880295L;
    }

    @Override
    public String getIdentifier() {
        return "pri_max";
    }

    @Override
    public Color getColor() {
        return Color.decode("#FF7042");
    }

    @Override
    public int getPriority() {
        return ReactionPriority.LOWEST;
    }
}
