package com.owen1212055.suggestionsbot.suggestions.reactions.misc;

import com.owen1212055.suggestionsbot.suggestions.reactions.Reaction;
import com.owen1212055.suggestionsbot.suggestions.reactions.ReactionPriority;

import java.awt.*;

public class Discussion extends Reaction {
    @Override
    public long getID() {
        return 612447965004693541L;
    }

    @Override
    public String getIdentifier() {
        return "discussion";
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
