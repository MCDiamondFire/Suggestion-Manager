package com.diamondfire.suggestionsbot.suggestions.suggestion;

import com.diamondfire.suggestionsbot.SuggestionsBot;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionHandler;
import com.diamondfire.suggestionsbot.suggestions.reactions.flag.ReactionFlag;
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
                .filter(reaction -> reaction.getReactionEmote().getIdLong() == SuggestionsBot.config.DOWNVOTE)
                .findFirst()
                .map(MessageReaction::getCount)
                .orElse(1) - 1;

        upVotes = reactions.stream()
                .filter(reaction -> reaction.getReactionEmote().getIdLong() == SuggestionsBot.config.UPVOTE)
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
