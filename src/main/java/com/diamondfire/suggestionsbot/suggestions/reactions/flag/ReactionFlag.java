package com.diamondfire.suggestionsbot.suggestions.reactions.flag;

import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;

public abstract class ReactionFlag extends Reaction {

    public abstract String getEmbedName();

    public abstract boolean preventPopular();


}
