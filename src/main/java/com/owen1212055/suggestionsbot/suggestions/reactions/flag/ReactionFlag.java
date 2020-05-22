package com.owen1212055.suggestionsbot.suggestions.reactions.flag;

import com.owen1212055.suggestionsbot.suggestions.reactions.Reaction;

public abstract class ReactionFlag extends Reaction {

    public abstract String getEmbedName();

    public abstract boolean preventPopular();


}
