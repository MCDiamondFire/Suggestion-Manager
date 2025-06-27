package com.diamondfire.suggestionsbot.command.impl;

import com.diamondfire.suggestionsbot.command.BotCommand;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.discord.jda5.JDAInteraction;
import org.jetbrains.annotations.NotNull;

public class WhenCommand implements BotCommand {

    @Override
    public Permission getPermission() {
        return Permission.USER;
    }

    @Command("when")
    @CommandDescription("When is my suggestion coming?")
    public void command(final @NotNull JDAInteraction interaction) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("When is it coming?");
        builder.addField("When the devs get to it.", "The devs are busy, they have a life and they decide when to add things as they please. Do not expect them to instantly add your cool suggestion.", true);
        IReplyCallback replyCallback = interaction.replyCallback();
        if (replyCallback == null) {
            return;
        }
        replyCallback.replyEmbeds(builder.build()).queue();
    }

}
