package com.diamondfire.suggestionsbot.suggestions.reactions.misc;

import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionPriority;

import java.awt.*;

public class PriorityMin extends Reaction {
    @Override
    public long getID() {
        return 612404608890962086L;
    }

    @Override
    public String getIdentifier() {
        return "pri_min";
    }

    @Override
    public Color getColor() {
        return Color.decode("#7053FF");
    }

    @Override
    public int getPriority() {
        return ReactionPriority.LOWEST;
    }
}
