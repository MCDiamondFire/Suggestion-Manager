package com.owen1212055.suggestionsbot.suggestions.suggestion;

import com.owen1212055.suggestionsbot.suggestions.reactions.Reaction;
import com.owen1212055.suggestionsbot.suggestions.reactions.ReactionHandler;
import com.owen1212055.suggestionsbot.suggestions.reactions.flag.ReactionFlag;
import com.owen1212055.suggestionsbot.util.BotConstants;
import net.dv8tion.jda.api.entities.MessageReaction;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReactionManager {


    private final int upVotes;
    private final int downVotes;
    private final List<Reaction> reactions;

    public ReactionManager(Suggestion suggestion) {
        List<MessageReaction> reactions = suggestion.getSuggestion().getReactions().stream()
                .filter(reaction -> reaction.getReactionEmote().isEmote())
                .collect(Collectors.toList());

        downVotes = reactions.stream()
                .filter(reaction -> reaction.getReactionEmote().getIdLong() == BotConstants.DOWNVOTE)
                .findFirst()
                .map(MessageReaction::getCount)
                .orElse(1) - 1;

        upVotes = reactions.stream()
                .filter(reaction -> reaction.getReactionEmote().getIdLong() == BotConstants.UPVOTE)
                .findFirst()
                .map(MessageReaction::getCount)
                .orElse(1) - 1;

        this.reactions = ReactionHandler.getReactions(suggestion.getSuggestion());

    }

    public int getNetVotes() {
        return upVotes - downVotes;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public Reaction getTopReaction() {
        return getReactions().stream().max(Comparator.comparingInt(Reaction::getPriority)).orElse(null);
    }

    public boolean canGoPopular() {
        return getReactions().stream().filter((reaction -> reaction instanceof ReactionFlag)).noneMatch((reaction -> ((ReactionFlag) reaction).preventPopular()));
    }
}
