package com.owen1212055.suggestionsbot.suggestions.reactions.misc;

import com.owen1212055.suggestionsbot.suggestions.reactions.Reaction;
import com.owen1212055.suggestionsbot.suggestions.reactions.ReactionPriority;

import java.awt.*;

public class PriorityMid extends Reaction {
    @Override
    public long getID() {
        return 612404618365894661L;
    }

    @Override
    public String getIdentifier() {
        return "pri_min";
    }

    @Override
    public Color getColor() {
        return Color.decode("#D95997");
    }

    @Override
    public int getPriority() {
        return ReactionPriority.LOWEST;
    }
}
