package com.diamondfire.suggestionsbot.suggestions.reactions.misc;

import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionPriority;

import java.awt.*;

public class NotDF extends Reaction {
    @Override
    public long getID() {
        return 612421502075404288L;
    }

    @Override
    public String getIdentifier() {
        return "notdf";
    }

    @Override
    public Color getColor() {
        return Color.decode("#854DF6");
    }

    @Override
    public int getPriority() {
        return ReactionPriority.LOWEST;
    }
}
