package com.diamondfire.suggestionsbot.suggestions.reactions.misc;

import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionPriority;

import java.awt.*;

public class ConfirmedIssue extends Reaction {
    @Override
    public long getID() { return 696873088612040706L; }

    @Override
    public String getIdentifier() {
        return "frm_iss";
    }

    @Override
    public Color getColor() {
        return Color.decode("#FFC843");
    }

    @Override
    public int getPriority() {
        return ReactionPriority.LOWEST;
    }
}
