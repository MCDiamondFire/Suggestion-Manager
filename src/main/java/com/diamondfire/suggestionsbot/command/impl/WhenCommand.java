package com.diamondfire.suggestionsbot.command.impl;

import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.command.permissions.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class WhenCommand extends BotCommand {

    @Override
    public String getName() {
        return "when";
    }

    @Override
    public String getDescription() {
        return "When is my suggestion coming?";
    }

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription());
    }

    @Override
    public Permission getPermission() {
        return Permissions.USER;
    }

    @Override
    public void run(SlashCommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("When is it coming?");
        builder.addField("When the devs get to it.", "The devs are busy, they have a life and they decide when to add things as they please. Do not expect them to instantly add your cool suggestion.", true);
        event.replyEmbeds(builder.build()).queue();
    }

}
