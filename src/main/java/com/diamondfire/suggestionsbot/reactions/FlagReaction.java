package com.diamondfire.suggestionsbot.reactions;

import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigFlagReaction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FlagReaction extends ResultReaction {

    private final String name;
    private final boolean preventPopular;

    public FlagReaction(ConfigFlagReaction config) {
        super(config);
        this.name = config.getName();
        this.preventPopular = config.isPreventPopular();

    }

    public String getName() {
        return this.name;
    }

    public boolean isPreventPopular() {
        return this.preventPopular;
    }

}
