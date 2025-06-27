package com.diamondfire.suggestionsbot.reactions;

import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigResultReaction;
import org.jspecify.annotations.NullMarked;

import java.awt.Color;

@NullMarked
public class ResultReaction extends Reaction {

    private final Color color;
    private final int priority;

    public ResultReaction(ConfigResultReaction config) {
        super(config);
        this.color = Color.decode(config.getColor());
        this.priority = config.getPriority();
    }

    public Color getColor() {
        return this.color;
    }

    public int getPriority() {
        return this.priority;
    }

}
