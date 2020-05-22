package com.owen1212055.suggestionsbot.command.commands;


import com.owen1212055.suggestionsbot.command.arguments.Argument;
import com.owen1212055.suggestionsbot.command.arguments.BasicIDArg;
import com.owen1212055.suggestionsbot.events.CommandEvent;
import com.owen1212055.suggestionsbot.instance.BotInstance;
import com.owen1212055.suggestionsbot.suggestions.suggestion.Suggestion;
import com.owen1212055.suggestionsbot.util.ConnectionProvider;
import net.dv8tion.jda.api.EmbedBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractSuggestionCommand extends Command {
    @Override
    public Argument getArgument() {
        return new BasicIDArg();
    }


    @Override
    public void run(CommandEvent event) {
        long message = 0;
        try {
            message = Long.parseLong(event.getArguments()[0]);
        } catch (NumberFormatException ignored) {
        }

        long channel = 0;

        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * from suggestions WHERE message = ?")) {
            statement.setLong(1, message);
            ResultSet table = statement.executeQuery();

            while (table.next()) {
                channel = table.getLong("message_channel");
            }

            table.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Suggestion suggestion = null;
        try {
            suggestion = new Suggestion(BotInstance.getJda().getTextChannelById(channel).retrieveMessageById(message).complete());
        } catch (Exception ignored) {

        }

        if (suggestion != null) {
            run(event, suggestion);
        } else {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Cannot find suggestion!");
            event.getChannel().sendMessage(builder.build()).queue();
        }

    }

    public abstract void run(CommandEvent event, Suggestion suggestion);
}
