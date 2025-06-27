package com.diamondfire.suggestionsbot.command.impl;

import com.diamondfire.suggestionsbot.command.BotCommand;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.database.SingleQueryBuilder;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.discord.jda5.JDAInteraction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class StatsCommand implements BotCommand {

    @Override
    public Permission getPermission() {
        return Permission.USER;
    }

    @Command("stats [user]")
    @CommandDescription("Gets statistics on a specific user.")
    public void command(final @NotNull JDAInteraction interaction, final @Argument("user") IMentionable user) {
        EmbedBuilder builder = new EmbedBuilder();
        final long id = interaction.user().getIdLong();
        if (user != null) {
            user.getIdLong();
        }

        new SingleQueryBuilder().query("SELECT * from suggestions WHERE author_id = ?", statement ->
                statement.setLong(1, id)
        ).onQuery(table -> {
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
                Reaction reaction = ReactionHandler.getReaction(reactName);
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

            builder.setAuthor(interaction.user().getName(), null, interaction.user().getEffectiveAvatarUrl());
            String[] stats = new String[]{
                    "Total Suggestions: " + suggestionCount,
                    "Total Upvotes: " + upVotes,
                    "Total Downvotes: " + downVotes
            };

            builder.addField("Suggestion Stats", String.join("\n", stats), true);

            builder.addField("Reaction Stats", reactionStat.entrySet()
                    .stream()
                    .map(reactionIntegerEntry -> reactionIntegerEntry.getKey().getJda().getFormatted() + ": " + reactionIntegerEntry.getValue())
                    .collect(Collectors.joining("\n")), true);

        }).onNotFound(() -> builder.setTitle("Player not found!")).execute();

        IReplyCallback replyCallback = interaction.replyCallback();
        if (replyCallback != null) {
            replyCallback.replyEmbeds(builder.build()).queue();
        }
    }

}
