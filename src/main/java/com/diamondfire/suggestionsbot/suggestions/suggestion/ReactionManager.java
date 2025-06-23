package com.diamondfire.suggestionsbot.suggestions.suggestion;

import com.diamondfire.suggestionsbot.suggestions.reactions.FlagReaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionHandler;
import com.diamondfire.suggestionsbot.suggestions.reactions.ResultReaction;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;

import java.util.Comparator;
import java.util.List;

public class ReactionManager {

    private final int upVotes;
    private final int downVotes;
    private final List<Reaction> reactions;

    public ReactionManager(Suggestion suggestion) {
        List<MessageReaction> reactions = suggestion.getSuggestion().getReactions().stream()
                .filter(reaction -> reaction.getEmoji() instanceof CustomEmoji)
                .toList();

        this.downVotes = reactions.stream()
                .filter(reaction -> {
                    Reaction react = ReactionHandler.getReaction(reaction);
                    return react != null && react.isDownvote();
                })
                .findFirst()
                .map(MessageReaction::getCount)
                .orElse(1) - 1;

        this.upVotes = reactions.stream()
                .filter(reaction -> {
                    Reaction react = ReactionHandler.getReaction(reaction);
                    return react != null && react.isUpvote();
                })
                .findFirst()
                .map(MessageReaction::getCount)
                .orElse(1) - 1;

        this.reactions = ReactionHandler.getReactions(suggestion.getSuggestion());

    }

    public int getNetVotes() {
        return this.upVotes - this.downVotes;
    }

    public int getUpVotes() {
        return this.upVotes;
    }

    public int getDownVotes() {
        return this.downVotes;
    }

    public List<Reaction> getReactions() {
        return this.reactions;
    }

    public ResultReaction getTopReaction() {
        return (ResultReaction) this.getReactions().stream()
                .filter(reaction -> reaction instanceof ResultReaction)
                .max(Comparator.comparingInt(reaction -> ((ResultReaction) reaction).getPriority()))
                .orElse(null);
    }

    public boolean canGoPopular() {
        return this.getReactions().stream()
                .filter(reaction -> reaction instanceof FlagReaction)
                .noneMatch(reaction -> ((FlagReaction) reaction).isPreventPopular());
    }

}
