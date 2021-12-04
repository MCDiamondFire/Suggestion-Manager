package com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference;

import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;

public abstract class Reference {
    Message reference;

    public Message getReference() {
        return reference;
    }

    public void setReference(Message reference) {
        this.reference = reference;
    }

    public long getID() {
        return reference.getIdLong();
    }

    public abstract String getName();

    public abstract long getChannelID();

    public abstract boolean syncEmojis();

    public abstract boolean mustExist();

    public abstract MessageEmbed create(Suggestion suggestion);

    public void refresh(Suggestion suggestion) {
        if (syncEmojis()) {
            List<Reaction> referenceReactions = new ArrayList<>(ReactionHandler.getReactions(getReference()));
            List<Reaction> suggestionReactions = suggestion.reactionManager.getReactions();

            for (Reaction reaction : referenceReactions) {
                if (!suggestionReactions.contains(reaction)) {
                    suggestion.getSuggestion().addReaction(reaction.getEmote()).complete();
                }
            }
            for (Reaction reaction : suggestionReactions) {
                if (!referenceReactions.contains(reaction)) {
                    getReference().addReaction(reaction.getEmote()).complete();
                }
            }

        }
        suggestion.refreshMessage();
        getReference().editMessageEmbeds(create(suggestion)).queue();

    }

    //Had to implement this because of reaction problems.
    public void plainRefresh(Suggestion suggestion) {

        suggestion.refreshMessage();
        getReference().editMessageEmbeds(create(suggestion)).queue();

    }
}
