package com.diamondfire.suggestionsbot.suggestions.suggestion;


import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.channels.Channel;
import com.diamondfire.suggestionsbot.suggestions.channels.ChannelHandler;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.ReferenceManager;
import com.diamondfire.suggestionsbot.util.ConnectionProvider;
import net.dv8tion.jda.api.entities.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Suggestion {

    public ReactionManager reactionManager;
    public DatabaseManager databaseManager;
    public ReferenceManager referenceManager;
    Message suggestion;
    Channel channel;


    public Suggestion(Message suggestion) {

        this.suggestion = suggestion;

        this.channel = ChannelHandler.getChannel(suggestion.getChannel().getIdLong());
        databaseManager = new DatabaseManager(this);
        reactionManager = new ReactionManager(this);
        referenceManager = new ReferenceManager(this);
    }

    // This looks everywhere for the suggestion, and if it cannot be found at all it returns null.

    public static Suggestion deepFind(Message message) {
        Suggestion suggestion = new Suggestion(message);
        if (suggestion.isValid()) {
            return suggestion;
        } else {
            try (Connection connection = ConnectionProvider.getConnection();
                 PreparedStatement statement = connection.prepareStatement(" SELECT * FROM suggestions WHERE popular_message = ? OR discussion_message = ?;")) {

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
                return messageID == 0 ? null : new Suggestion(BotInstance.getJda().getTextChannelById(channelID).retrieveMessageById(messageID).complete());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public Message getSuggestion() {
        return suggestion;
    }

    public Channel getChannel() {
        return channel;
    }

    public boolean isValid() {
        return databaseManager.exists();
    }


    public void refreshMessage() {
        suggestion = suggestion.getChannel().retrieveMessageById(suggestion.getIdLong()).complete();
        reactionManager = new ReactionManager(this);
    }
}
