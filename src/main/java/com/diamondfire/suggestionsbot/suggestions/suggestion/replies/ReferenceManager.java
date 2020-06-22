package com.diamondfire.suggestionsbot.suggestions.suggestion.replies;

import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.Reference;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types.DiscussionReference;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types.PopularReference;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

//This class handles replies and stuff like popular message msgs, discussion msg, etc.

public class ReferenceManager {

    private final Suggestion suggestion;
    private final HashMap<String, Reference> references = new HashMap<>();

    public ReferenceManager(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public void newReference(Reference reference) {
        if (references.get(reference.getName()) != null) return;


        TextChannel channel = BotInstance.getJda().getTextChannelById(reference.getChannelID());

        channel.sendMessage(reference.create(suggestion)).queue((message) -> {
            // Set the reference to the newly defined message.
            reference.setReference(message);
            references.put(reference.getName(), reference);
            suggestion.databaseManager.refreshDBEntry();
        });


    }

    public void refreshReferences() {
        fetchReferences().thenRunAsync(() -> {
            for (Reference reference : references.values()) {
                reference.refresh(suggestion);
            }
        });
    }

    public void plainRefresh() {
        fetchReferences().thenRunAsync(() -> {
            for (Reference reference : references.values()) {
                reference.plainRefresh(suggestion);
            }
        });
    }

    public CompletableFuture<Void> fetchReferences() {
        return CompletableFuture.runAsync(() -> {
            fetchReference(new PopularReference());
            fetchReference(new DiscussionReference());
        });
    }

    private void fetchReference(Reference reference) {
        long messageID = suggestion.databaseManager.getReference(reference.getName());
        // Make sure to ignore lost references.
        if (messageID == 0) {
            return;
        }

        Message message = BotInstance.getJda().getTextChannelById(reference.getChannelID()).retrieveMessageById(messageID).complete();
        if (message == null) {
            if (reference.mustExist()) {
                newReference(reference);
            }
            return;
        }
        reference.setReference(message);
        references.put(reference.getName(), reference);

    }

    public HashMap<String, Reference> getReferences() {
        return references;
    }

    public Reference getReference(String name) {
        return references.get(name);
    }

    // Fetches the message ID of the reference.
    public long getReferenceLong(String name) {
        Reference reference = references.get(name);
        if (reference == null) return 0;

        return reference.getID();
    }

    public void removeReaction(MessageReaction.ReactionEmote reactionEmote) {
        suggestion.getSuggestion().clearReactions(reactionEmote.getEmote()).complete();
        getReferences().values().forEach((reference -> {
            reference.getReference().clearReactions(reactionEmote.getEmote()).complete();
        }));
    }


}
