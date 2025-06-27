package com.diamondfire.suggestionsbot.suggestion;

import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.channels.ChannelHandler;
import com.diamondfire.suggestionsbot.channels.SuggestionsChannel;
import com.diamondfire.suggestionsbot.database.ConnectionProvider;
import com.diamondfire.suggestionsbot.suggestion.replies.ReferenceManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NullMarked
public class Suggestion {

    private final DatabaseManager databaseManager;
    private final ReferenceManager referenceManager;
    private final SuggestionsChannel suggestionsChannel;
    private ReactionManager reactionManager;
    private Message suggestion;

    public Suggestion(Message suggestion) {
        this.suggestion = suggestion;

        this.suggestionsChannel = ChannelHandler.getChannel(suggestion.getChannel().getIdLong());
        this.reactionManager = new ReactionManager(this);
        this.databaseManager = new DatabaseManager(this);
        this.referenceManager = new ReferenceManager(this);
    }

    // This looks everywhere for the suggestion, and if it cannot be found at all it returns null.

    @Nullable
    public static Suggestion deepFind(Message message) {
        Suggestion suggestion = new Suggestion(message);
        if (suggestion.isValid()) {
            return suggestion;
        } else {
            String sql = "SELECT * FROM suggestions WHERE popular_message = ? OR discussion_message = ?;";
            try (Connection connection = ConnectionProvider.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setLong(1, message.getIdLong());
                statement.setLong(2, message.getIdLong());
                statement.execute();

                ResultSet rs = statement.executeQuery();

                long messageId = 0;
                long channelId = 0;
                if (rs.next()) {
                    messageId = rs.getLong("message");
                    channelId = rs.getLong("message_channel");
                }
                rs.close();
                TextChannel channel = BotInstance.getJda().getTextChannelById(channelId);
                if (channel == null) {
                    return null;
                }
                return new Suggestion(channel.retrieveMessageById(messageId).complete());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Message getSuggestion() {
        return this.suggestion;
    }

    public SuggestionsChannel getSuggestionsChannel() {
        return this.suggestionsChannel;
    }

    public boolean isValid() {
        return this.databaseManager.exists();
    }

    public void refreshMessage() {
        this.suggestion = this.suggestion.getChannel().retrieveMessageById(this.suggestion.getIdLong()).complete();
        this.reactionManager = new ReactionManager(this);
    }

    public ReactionManager getReactionManager() {
        return this.reactionManager;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public ReferenceManager getReferenceManager() {
        return this.referenceManager;
    }

}
