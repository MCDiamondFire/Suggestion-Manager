package com.diamondfire.suggestionsbot.suggestions.reactions;

import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigDownvoteReaction;
import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigReaction;
import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigUpvoteReaction;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Reaction {

    private final String identifier;
    private final long emoji;
    private final Type type;
    private final Emoji jda;

    public Reaction(ConfigReaction config) {
        this.identifier = config.getIdentifier();
        this.emoji = config.getEmoji();
        if (config instanceof ConfigUpvoteReaction) {
            this.type = Type.UPVOTE;
        } else if (config instanceof ConfigDownvoteReaction) {
            this.type = Type.DOWNVOTE;
        } else {
            this.type = Type.NORMAL;
        }
        RichCustomEmoji customEmoji = BotInstance.getJda().getEmojiById(this.emoji);
        if (customEmoji == null) {
            throw new IllegalArgumentException("Emoji ID " + this.emoji + " is null");
        }
        this.jda = customEmoji;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public long getEmoji() {
        return this.emoji;
    }

    public boolean isUpvote() {
        return this.type == Type.UPVOTE;
    }

    public boolean isDownvote() {
        return this.type == Type.DOWNVOTE;
    }

    public boolean isNetScore() {
        return this.isUpvote() || this.isDownvote();
    }

    public Emoji getJda() {
        return this.jda;
    }

    private enum Type {
        NORMAL,
        UPVOTE,
        DOWNVOTE
    }

}
