package com.diamondfire.suggestionsbot.suggestions.channels;

import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.reactions.PopularHandler;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionHandler;
import com.diamondfire.suggestionsbot.suggestions.suggestion.ReactionManager;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.ReferenceManager;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types.DiscussionReference;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types.PopularReference;
import com.diamondfire.suggestionsbot.util.config.type.ConfigSuggestionsChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.jspecify.annotations.NullMarked;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@NullMarked
public class SuggestionsChannel {

    private static final int REACTION_ADD_DELAY_MILLIS = 100;

    private final long id;
    private final String postName;
    private final boolean canGoPopular;
    private final List<Reaction> reactions;
    private final TextChannel jda;

    public SuggestionsChannel(ConfigSuggestionsChannel config) {
        this.id = config.getId();
        this.postName = config.getPostName();
        this.canGoPopular = config.isCanGoPopular();
        ArrayList<Reaction> reacts = new ArrayList<>();
        for (String identifier : config.getEmojis()) {
            reacts.add(ReactionHandler.getReaction(identifier));
        }
        this.reactions = Collections.unmodifiableList(reacts);

        TextChannel textChannel = BotInstance.getJda().getChannelById(TextChannel.class, this.id);
        if (textChannel == null) {
            throw new IllegalArgumentException("Channel ID " + this.id + " is null");
        }
        this.jda = textChannel;
    }

    public void onMessage(Message message) {
        for (Reaction reaction : this.reactions) {
            message.addReaction(reaction.getJDA()).queueAfter(REACTION_ADD_DELAY_MILLIS, TimeUnit.MILLISECONDS);
        }

        // Here we initialize a new suggestion and add it to the database. After, we can discard it...
        Suggestion suggestion = new Suggestion(message);
        suggestion.getDatabaseManager().addToDatabase();
        suggestion.getReferenceManager().newReference(new DiscussionReference(message.getGuildId()));

    }

    public void onSuggestionReaction(Suggestion suggestion, MessageReactionAddEvent event) {
        Message message = suggestion.getSuggestion();

        LocalDate currentDate = LocalDate.now();
        Date d1 = Date.from(currentDate.minusDays(27).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date d2 = new Date(message.getTimeCreated().toEpochSecond() * 1000);


        // If it's a valid suggestion, try to popularize it and give reaction stuff.
        ReactionManager reactionManager = suggestion.getReactionManager();
        ReferenceManager referenceManager = suggestion.getReferenceManager();
        if (reactionManager.getNetVotes() >= PopularHandler.ratio && reactionManager.canGoPopular() && suggestion.getSuggestionsChannel().canGoPopular && ChannelHandler.getSuggestionsChannelOrNull(event.getChannel().getIdLong()) != null) {
            if (d2.after(d1)) {
                referenceManager.newReference(new PopularReference(message.getGuildId()));
            } else if (reactionManager.getNetVotes() == PopularHandler.ratio) {
                referenceManager.newReference(new PopularReference(message.getGuildId()));
            }

        }
        //Refreshes references to try to react correctly
        suggestion.getDatabaseManager().refreshDBEntry();
        referenceManager.refreshReferences();
    }

    public long getId() {
        return this.id;
    }

    public String getPostName() {
        return this.postName;
    }

    public boolean isCanGoPopular() {
        return this.canGoPopular;
    }

    public List<Reaction> getReactions() {
        return this.reactions;
    }

    public TextChannel getJDA() {
        return this.jda;
    }

}


