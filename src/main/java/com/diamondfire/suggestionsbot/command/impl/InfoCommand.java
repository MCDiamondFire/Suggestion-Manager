package com.diamondfire.suggestionsbot.command.impl;


import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.command.BotCommand;
import com.diamondfire.suggestionsbot.database.SingleQueryBuilder;
import com.diamondfire.suggestionsbot.guild.BotGuilds;
import com.diamondfire.suggestionsbot.suggestion.Suggestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.parser.Parser;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.discord.jda5.JDAInteraction;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class InfoCommand implements BotCommand {

    @Override
    public int getPermissionLevel() {
        return 1;
    }

    @Parser(name = "suggestion")
    public Suggestion suggestion(CommandInput input) {
        long messageId = input.readLong();
        AtomicReference<Suggestion> suggestion = new AtomicReference<>();

        new SingleQueryBuilder().query("SELECT message_channel from suggestions WHERE message = ?", statement ->
                statement.setLong(1, messageId)
        ).onQuery(set -> {
            long channel = set.getLong("message_channel");
            TextChannel textChannel = BotInstance.getJda().getTextChannelById(channel);
            if (textChannel == null) {
                return;
            }
            suggestion.set(new Suggestion(textChannel.retrieveMessageById(messageId).complete()));
        }).execute();

        return suggestion.get();
    }

    @Command("info [suggestion]")
    @CommandDescription("Gets current open references to a specific message.")
    public void suggestionInfo(final @NotNull JDAInteraction interaction, @Argument(value = "suggestion", parserName = "suggestion") Suggestion suggestion) {
        if (suggestion == null) {
            this.showUserInfo(interaction);
            return;
        }
        EmbedBuilder builder = new EmbedBuilder();
        builder.addField("References: ", String.join("\n", suggestion.getReferenceManager().getReferences().keySet()), true);

        IReplyCallback replyCallback = interaction.replyCallback();
        if (replyCallback == null) {
            return;
        }
        replyCallback.replyEmbeds(builder.build()).queue();
    }

    public void showUserInfo(final @NotNull JDAInteraction interaction) {
        EmbedBuilder builder = new EmbedBuilder();
        Guild guild = interaction.guild();
        if (guild == null) {
            return;
        }
        builder.addField("Your Permission Level", "" + BotGuilds.get(guild).getPermissionLevel(interaction.user()), true);

        IReplyCallback replyCallback = interaction.replyCallback();
        if (replyCallback == null) {
            return;
        }
        replyCallback.replyEmbeds(builder.build()).queue();
    }

}
