package com.diamondfire.suggestionsbot.command.commands;


import com.diamondfire.suggestionsbot.command.arguments.Argument;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import com.diamondfire.suggestionsbot.util.ConnectionProvider;
import com.diamondfire.suggestionsbot.command.arguments.BasicIDArg;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class StatsCommand extends Command {
    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "Gets stats on a specific user";
    }

    @Override
    public Argument getArgument() {
        return new BasicIDArg();
    }

    @Override
    public Permission getPermission() {
        return Permission.USER;
    }

    @Override
    public void run(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        long id = 0;
        try {
            id = Long.parseLong(event.getArguments()[0]);
        } catch (NumberFormatException ignored) {
        }


        int upVotes = 0;
        int downVotes = 0;
        int suggestionCount = 0;
        ArrayList<String> reactions = new ArrayList<>();

        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * from suggestions WHERE author_id = ?")) {
            statement.setLong(1, id);
            ResultSet table = statement.executeQuery();

            while (table.next()) {
                suggestionCount++;
                upVotes += table.getInt("upvotes");
                downVotes += table.getInt("downvotes");
                reactions.addAll(Arrays.asList(table.getString("special_reactions").split(",")));
            }

            table.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        HashMap<Reaction, Integer> reactionStat = new HashMap<>();

        for (String reactName : reactions) {
            Reaction reaction = ReactionHandler.getFromIdentifier(reactName);
            if (reaction == null) {
                continue;
            }
            if (!reactionStat.containsKey(reaction)) {
                reactionStat.put(reaction, 1);
            } else {
                int i = reactionStat.get(reaction) + 1;
                reactionStat.replace(reaction, i);
            }

        }
        builder.setTitle("Stats:");

        User user = BotInstance.getJda().retrieveUserById(id).complete();
        builder.setAuthor(user.getName(), null, user.getEffectiveAvatarUrl());
        String[] stats = new String[]{
                "Total Suggestions: " + suggestionCount,
                "Total Upvotes: " + upVotes,
                "Total Downvotes: " + downVotes,

        };
        builder.addField("Suggestion Stats", String.join("\n", stats), true);

        builder.addField("Reaction Stats", reactionStat.entrySet()
                .stream()
                .map((reactionIntegerEntry -> reactionIntegerEntry.getKey().getEmote().getAsMention() + ": " + reactionIntegerEntry.getValue()))
                .collect(Collectors.joining("\n")), true);

        event.getChannel().sendMessage(builder.build()).queue();

    }
}
