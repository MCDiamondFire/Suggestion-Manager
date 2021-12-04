package com.diamondfire.suggestionsbot.command.impl;

import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.command.permissions.Permissions;
import com.diamondfire.suggestionsbot.command.permissions.PermissionHandler;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;


public class HelpCommand extends BotCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Lists all available commands.";
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
        builder.setTitle("Help");
        builder.setThumbnail(BotInstance.getJda().getSelfUser().getAvatarUrl());
        builder.setFooter("Your permissions: " + PermissionHandler.getPermission(event.getMember()).getName());

        BotInstance.getHandler().getCommandMap().values().stream()
                .filter((command) -> command.getPermission().hasPermission(event.getMember()))
                .forEach(command -> builder.addField("/" + command.getName(), command.getDescription(), false));

        event.replyEmbeds(builder.build()).queue();
    }
}
