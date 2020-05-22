package com.owen1212055.suggestionsbot.command.commands;


import com.owen1212055.suggestionsbot.command.arguments.Argument;
import com.owen1212055.suggestionsbot.command.arguments.BasicIDArg;
import com.owen1212055.suggestionsbot.command.permissions.Permission;
import com.owen1212055.suggestionsbot.events.CommandEvent;
import com.owen1212055.suggestionsbot.util.ConnectionProvider;
import net.dv8tion.jda.api.EmbedBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteCommand extends Command {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Removes a suggestion based on the given ID. ";
    }

    @Override
    public Argument getArgument() {
        return new BasicIDArg();
    }

    @Override
    public Permission getPermission() {
        return Permission.MOD;
    }

    @Override
    public void run(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        long id = 0;

        try {
            id = Long.parseLong(event.getArguments()[0]);
        } catch (NumberFormatException ignored) {
        }

        int effected = 0;
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM suggestions WHERE message = ?;")) {

            statement.setLong(1, id);
            effected = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        builder.setTitle("Affected " + effected + " rows...");
        event.getChannel().sendMessage(builder.build()).queue();


    }
}
