package com.owen1212055.suggestionsbot.command.commands;

import com.owen1212055.suggestionsbot.command.arguments.Argument;
import com.owen1212055.suggestionsbot.command.arguments.NoArg;
import com.owen1212055.suggestionsbot.command.permissions.Permission;
import com.owen1212055.suggestionsbot.events.CommandEvent;
import com.owen1212055.suggestionsbot.instance.BotInstance;
import com.owen1212055.suggestionsbot.suggestions.suggestion.Suggestion;
import com.owen1212055.suggestionsbot.util.ConnectionProvider;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.sql.*;

public class RefreshCommand extends Command {

    @Override
    public String getName() {
        return "refresh";
    }

    @Override
    public String getDescription() {
        return "Forces the bot to refresh and examine all suggestions that are currently stored in the database." +
                "\nSuggestions which have their main suggestion deleted are permanently lost, and are deleted." +
                "\nReferences that are required are regenerated.";
    }

    @Override
    public Argument getArgument() {
        return new NoArg();
    }

    @Override
    public Permission getPermission() {
        return Permission.MOD;
    }

    @Override
    public void run(CommandEvent event) {
        new Thread(() -> {

            int deleted = 0;
            int refreshed = 0;

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Refreshing...");

            Message message = event.getChannel().sendMessage(builder.build()).complete();

            try (Connection connection = ConnectionProvider.getConnection();
                 Statement query = connection.createStatement()) {
                String sql = ("SELECT * from suggestions;");
                ResultSet rs = query.executeQuery(sql);
                while (rs.next()) {
                    long suggestionID = rs.getLong("message");
                    long suggestionChannel = rs.getLong("message_channel");

                    Message suggestionMsg = null;
                    try {
                        suggestionMsg = BotInstance.getJda().getTextChannelById(suggestionChannel).retrieveMessageById(suggestionID).complete(true);
                    } catch (Exception ignored) {
                    }

                    if (suggestionMsg == null) {
                        try (Connection deleteConnection = ConnectionProvider.getConnection();
                             PreparedStatement deleteQuery = deleteConnection.prepareStatement("DELETE from suggestions WHERE message = ?");) {


                            deleteQuery.setLong(1, suggestionID);
                            deleteQuery.execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        deleted++;
                        continue;
                    }

                    Suggestion suggestion = new Suggestion(suggestionMsg);

                    suggestion.referenceManager.refreshReferences();
                    suggestion.databaseManager.refreshDBEntry();

                    refreshed++;

                }
                rs.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

            builder.setTitle("Refreshed Database!");
            builder.setDescription(String.format("%s suggestions have been refreshed and %s have been deleted.", refreshed, deleted));

            message.editMessage(builder.build()).queue();
        }).start();
    }
}
