package com.diamondfire.suggestionsbot.suggestions.channels;

import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types.DiscussionReference;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types.PopularReference;
import com.diamondfire.suggestionsbot.suggestions.reactions.PopularHandler;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public abstract class Channel {

    public abstract String getName();

    public abstract long getID();

    public abstract boolean canGoPopular();

    protected abstract Emote[] getEmotes();

    public void onMessage(Message message) {
        Stream.of(getEmotes()).forEachOrdered((reaction) -> message.addReaction(reaction).queueAfter(500, TimeUnit.MILLISECONDS));

        // Here we initialize a new suggestion and add it to the database. After, we can discard it...
        Suggestion suggestion = new Suggestion(message);
        suggestion.databaseManager.addToDatabase();
        suggestion.referenceManager.newReference(new DiscussionReference());

    }

    public void onSuggestionReaction(Suggestion suggestion, GuildMessageReactionAddEvent event) {
        Message message = suggestion.getSuggestion();

        LocalDate currentDate = LocalDate.now();
        Date d1 = Date.from(currentDate.minusDays(27).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date d2 = new Date(message.getTimeCreated().toEpochSecond() * 1000);


        // If it's a valid suggestion, try to popularize it and give reaction stuff.
        if (suggestion.reactionManager.getNetVotes() >= PopularHandler.ratio && suggestion.reactionManager.canGoPopular() && suggestion.getChannel().canGoPopular() && ChannelHandler.isValidChannel(event.getChannel().getIdLong())) {
            if (d2.after(d1)) {
                suggestion.referenceManager.newReference(new PopularReference());
            } else if (suggestion.reactionManager.getNetVotes() == PopularHandler.ratio) {
                suggestion.referenceManager.newReference(new PopularReference());
            }

        }
        //Refreshes references to try to react correctly
        suggestion.databaseManager.refreshDBEntry();
        suggestion.referenceManager.refreshReferences();


    }
}


