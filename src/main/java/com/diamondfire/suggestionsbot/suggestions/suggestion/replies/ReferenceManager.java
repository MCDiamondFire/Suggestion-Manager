package com.diamondfire.suggestionsbot.suggestions.suggestion.replies;

import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.Reference;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types.DiscussionReference;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types.PopularReference;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;

import java.util.HashMap;

//This class handles replies and stuff like popular message msgs, discussion msg, etc.

public class ReferenceManager {

    private final Suggestion suggestion;
    private final HashMap<String, Reference> references = new HashMap<>();

    public ReferenceManager(Suggestion suggestion) {

        this.suggestion = suggestion;

        this.fetchReference(new PopularReference(suggestion.getSuggestion().getGuildId()));
        this.fetchReference(new DiscussionReference(suggestion.getSuggestion().getGuildId()));


    }

    public void newReference(Reference reference) {
        if (this.references.get(reference.getName()) != null) {
            return;
        }

        TextChannel channel = BotInstance.getJda().getTextChannelById(reference.getChannelID());
        if (channel == null) {
            return;
        }

        channel.sendMessageEmbeds(reference.create(this.suggestion)).queue(message -> {
            // Set the reference to the newly defined message.
            reference.setReference(message);
            this.references.put(reference.getName(), reference);
            this.suggestion.getDatabaseManager().refreshDBEntry();
        });


    }

    public void refreshReferences() {
        for (Reference reference : this.references.values()) {
            reference.refresh(this.suggestion);
        }
    }

    public void plainRefresh() {
        for (Reference reference : this.references.values()) {
            reference.plainRefresh(this.suggestion);
        }
    }

    private void fetchReference(Reference reference) {
        long messageID = this.suggestion.getDatabaseManager().getReference(reference.getName());

        // Make sure to ignore lost references.
        if (messageID == 0) {
            return;
        }

        Message message = null;
        try {
            message = BotInstance.getJda().getTextChannelById(reference.getChannelID()).retrieveMessageById(messageID).complete(true);
        } catch (Exception ignored) {
        }

        // If message cannot be found, try to create a new one. (if it must exist)
        if (message == null) {
            if (reference.mustExist()) {
                this.newReference(reference);
            }
            return;
        }

        reference.setReference(message);

        this.references.put(reference.getName(), reference);


    }

    public HashMap<String, Reference> getReferences() {
        return this.references;
    }

    public Reference getReference(String name) {
        return this.references.get(name);
    }

    // Fetches the message ID of the reference.
    public long getReferenceLong(String name) {
        Reference reference = this.references.get(name);
        if (reference == null) {
            return 0;
        }

        return reference.getID();
    }

    public void removeReaction(EmojiUnion reactionEmote) {
        this.suggestion.getSuggestion().clearReactions(reactionEmote).complete();
        this.getReferences().values().forEach(reference -> {
            reference.getReference().clearReactions(reactionEmote).complete();
        });
    }


}
