package com.diamondfire.suggestionsbot.suggestions.suggestion;


import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.database.ConnectionProvider;
import com.diamondfire.suggestionsbot.suggestions.channels.ChannelHandler;
import com.diamondfire.suggestionsbot.suggestions.channels.SuggestionsChannel;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.ReferenceManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Suggestion {

    private ReactionManager reactionManager;
    private final DatabaseManager databaseManager;
    private final ReferenceManager referenceManager;
    private Message suggestion;
    private final SuggestionsChannel suggestionsChannel;

    public Suggestion(Message suggestion) {
        this.suggestion = suggestion;

        SuggestionsChannel channel = ChannelHandler.getChannel(suggestion.getChannel().getIdLong());
        this.suggestionsChannel = channel;
        this.reactionManager = new ReactionManager(this);
        this.databaseManager = new DatabaseManager(this);
        this.referenceManager = new ReferenceManager(this);
    }

    // This looks everywhere for the suggestion, and if it cannot be found at all it returns null.

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

                long messageID = 0;
                long channelID = 0;
                if (rs.next()) {
                    messageID = rs.getLong("message");
                    channelID = rs.getLong("message_channel");
                }
                rs.close();
                TextChannel channel = BotInstance.getJda().getTextChannelById(channelID);
                if (channel == null) {
                    return null;
                }
                return new Suggestion(channel.retrieveMessageById(messageID).complete());
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
