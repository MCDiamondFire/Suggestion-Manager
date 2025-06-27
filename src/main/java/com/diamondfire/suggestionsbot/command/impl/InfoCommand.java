package com.diamondfire.suggestionsbot.command.impl;


import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.command.BotCommand;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.database.SingleQueryBuilder;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.checkerframework.checker.units.qual.C;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.parser.Parser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.discord.jda5.JDAInteraction;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class InfoCommand implements BotCommand {

    @Override
    public Permission getPermission() {
        return Permission.MOD;
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

    @Command("info <suggestion>")
    @CommandDescription("Gets current open references to a specific message.")
    public void command(final @NotNull JDAInteraction interaction, @Argument(value = "suggestion", parserName = "suggestion") Suggestion suggestion) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.addField("References: ", String.join("\n", suggestion.getReferenceManager().getReferences().keySet()), true);

        IReplyCallback replyCallback = interaction.replyCallback();
        if (replyCallback == null) {
            return;
        }
        replyCallback.replyEmbeds(builder.build()).queue();
    }

}
