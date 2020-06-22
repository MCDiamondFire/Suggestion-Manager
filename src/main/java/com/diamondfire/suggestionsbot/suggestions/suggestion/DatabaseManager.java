package com.diamondfire.suggestionsbot.suggestions.suggestion;

import com.diamondfire.suggestionsbot.database.ConnectionProvider;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import net.dv8tion.jda.api.entities.Message;

import java.sql.*;
import java.util.stream.Collectors;

//TODO Migrate to the new query system
public class DatabaseManager {
    private final Suggestion suggestion;


    public DatabaseManager(Suggestion suggestion) {
        this.suggestion = suggestion;

    }

    public void addToDatabase() {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO suggestions (message, message_channel, popular_message, discussion_message, author_id, date, upvotes, downvotes, special_reactions) VALUES(?,?,?,?,?,?,?,?,?)")) {

            handleStatement(preparedStatement);

            preparedStatement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshDBEntry() {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE suggestions SET message = ?, message_channel = ?, popular_message = ?, discussion_message = ?, author_id = ?, date = ?, upvotes = ?, downvotes = ?, special_reactions = ? WHERE message = ?")) {

            handleStatement(statement);
            statement.setLong(10, suggestion.getSuggestion().getIdLong());

            statement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean exists() {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(" SELECT * FROM suggestions WHERE message = ?;")) {

            statement.setLong(1, suggestion.getSuggestion().getIdLong());

            ResultSet rs = statement.executeQuery();

            boolean exists = rs.next();

            rs.close();

            return exists;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //TODO Instead of fetching each reference individually, do it all at once.
    public long getReference(String tableName) {
        try (Connection connection = ConnectionProvider.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM suggestions WHERE message = ?;")) {

            statement.setLong(1, suggestion.getSuggestion().getIdLong());
            statement.execute();

            ResultSet rs = statement.executeQuery();

            long referenceLong = 0;
            if (rs.next()) {
                try {
                    referenceLong = rs.getLong(tableName);
                } catch (SQLException ignored) {
                    ignored.printStackTrace();
                }

            }
            rs.close();


            return referenceLong;


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return 0;
    }


    private void handleStatement(PreparedStatement statement) throws SQLException {
        Message message = suggestion.getSuggestion();
        ReactionManager manager = suggestion.reactionManager;

        statement.setLong(1, message.getIdLong());
        statement.setLong(2, message.getChannel().getIdLong());
        statement.setLong(3, suggestion.referenceManager.getReferenceLong("popular_message"));
        statement.setLong(4, suggestion.referenceManager.getReferenceLong("discussion_message"));
        statement.setLong(5, message.getAuthor().getIdLong());
        statement.setTimestamp(6, Timestamp.from(message.getTimeCreated().toInstant()));
        statement.setInt(7, manager.getUpVotes());
        statement.setInt(8, manager.getDownVotes());
        statement.setString(9, getFormattedReactions());
    }


    private String getFormattedReactions() {
        return suggestion.reactionManager.getReactions().stream().map((Reaction::getIdentifier)).collect(Collectors.joining(","));
    }


}
