package com.diamondfire.suggestionsbot.suggestions.reactions.flag.accept;

import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionPriority;
import com.diamondfire.suggestionsbot.suggestions.reactions.flag.ReactionFlag;

import java.awt.*;

public class Patched extends ReactionFlag {
    @Override
    public Color getColor() {
        return Color.decode("#42C757");
    }

    @Override
    public String getEmbedName() {
        return "Marked as Patched";
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
        return 612434701927448604L;
    }

    @Override
    public String getIdentifier() {
        return "patched";
    }
}
