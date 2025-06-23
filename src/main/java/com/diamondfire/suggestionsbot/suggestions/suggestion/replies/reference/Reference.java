package com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference;

import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionHandler;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;

public abstract class Reference {

    private Message reference;

    public Message getReference() {
        return this.reference;
    }

    public void setReference(Message reference) {
        this.reference = reference;
    }

    public long getId() {
        return this.reference.getIdLong();
    }

    public abstract String getName();

    public abstract long getChannelId();

    public abstract boolean syncEmojis();

    public abstract boolean mustExist();

    public abstract MessageEmbed create(Suggestion suggestion);

    public void refresh(Suggestion suggestion) {
        if (this.syncEmojis()) {
            List<Reaction> referenceReactions = new ArrayList<>(ReactionHandler.getReactions(this.getReference()));
            List<Reaction> suggestionReactions = suggestion.getReactionManager().getReactions();

            for (Reaction reaction : referenceReactions) {
                if (!suggestionReactions.contains(reaction)) {
                    suggestion.getSuggestion().addReaction(reaction.getJda()).complete();
                }
            }
            for (Reaction reaction : suggestionReactions) {
                if (!referenceReactions.contains(reaction)) {
                    this.getReference().addReaction(reaction.getJda()).complete();
                }
            }

        }
        suggestion.refreshMessage();
        this.getReference().editMessageEmbeds(this.create(suggestion)).queue();

    }

    //Had to implement this because of reaction problems.
    public void plainRefresh(Suggestion suggestion) {
        suggestion.refreshMessage();
        this.getReference().editMessageEmbeds(this.create(suggestion)).queue();
    }

}
