package com.diamondfire.suggestionsbot.suggestions.reactions.misc;

import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionPriority;

import java.awt.*;

public class PriorityMid extends Reaction {
    @Override
    public long getID() {
        return 612404618365894661L;
    }

    @Override
    public String getIdentifier() {
        return "pri_mid";
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
