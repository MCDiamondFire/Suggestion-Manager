package com.diamondfire.suggestionsbot.suggestion.replies.reference;

import com.diamondfire.suggestionsbot.reactions.ReactionHandler;
import com.diamondfire.suggestionsbot.reactions.ResultReaction;
import com.diamondfire.suggestionsbot.suggestion.Suggestion;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

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

    public abstract TextChannel getChannel();

    public abstract boolean syncEmojis();

    public abstract boolean mustExist();

    public abstract MessageEmbed create(Suggestion suggestion);

    public void refresh(Suggestion suggestion) {
        if (this.syncEmojis()) {
            List<ResultReaction> referenceReactions = new ArrayList<>(ReactionHandler.getReactions(this.getReference()));
            List<ResultReaction> suggestionReactions = suggestion.getReactionManager().getReactions();

            for (ResultReaction reaction : referenceReactions) {
                if (!suggestionReactions.contains(reaction)) {
                    suggestion.getSuggestion().addReaction(reaction.getJda()).complete();
                }
            }
            for (ResultReaction reaction : suggestionReactions) {
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
