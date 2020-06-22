package com.diamondfire.suggestionsbot.command.impl;


import com.diamondfire.suggestionsbot.command.arguments.value.LongArg;
import com.diamondfire.suggestionsbot.command.arguments.value.ValueArgument;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.database.SingleQueryBuilder;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

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
    public ValueArgument<Long> getArgument() {
        return new LongArg("User ID", false);
    }

    @Override
    public Permission getPermission() {
        return Permission.USER;
    }

    @Override
    public void run(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        long id = 0;
        if (event.getParsedArgs().isEmpty()) {
            id = event.getAuthor().getIdLong();
        } else {
            id = getArgument().getArg(event.getParsedArgs());
        }


        long finalId = id;
        new SingleQueryBuilder().query("SELECT * from suggestions WHERE author_id = ?", (statement -> {
            statement.setLong(1, finalId);
        })).onQuery((table) -> {
            int suggestionCount = 0;
            int upVotes = 0;
            int downVotes = 0;
            ArrayList<String> reactions = new ArrayList<>();

            do {
                suggestionCount++;
                upVotes += table.getInt("upvotes");
                downVotes += table.getInt("downvotes");
                reactions.addAll(Arrays.asList(table.getString("special_reactions").split(",")));

            } while (table.next());
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

            User user = BotInstance.getJda().retrieveUserById(finalId).complete();
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

        }).onNotFound(() -> {
            builder.setTitle("Player not found!");
        }).execute();

        event.getChannel().sendMessage(builder.build()).queue();


    }
}
