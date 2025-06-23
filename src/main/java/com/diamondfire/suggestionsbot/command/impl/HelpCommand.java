package com.diamondfire.suggestionsbot.command.impl;

import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.command.argument.ArgumentSet;
import com.diamondfire.suggestionsbot.command.help.HelpContext;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.command.permissions.PermissionHandler;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import com.diamondfire.suggestionsbot.util.BotConstants;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.stream.Collectors;


public class HelpCommand extends Command {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Lists all available commands.");
    }

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet();
    }

    @Override
    public Permission getPermission() {
        return Permission.USER;
    }

    @Override
    public void run(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Help");
        builder.setThumbnail(BotInstance.getJda().getSelfUser().getAvatarUrl());
        builder.setFooter("Your permissions: " + PermissionHandler.getPermission(event.getMember()));

        BotInstance.getHandler().getCommands().values().stream()
                .filter((command) -> command.getPermission().hasPermission(event.getMember()))
                .forEach(command -> builder.addField(BotConstants.PREFIX + command.getName() + " " + generateArguments(command.getHelpContext()), command.getHelpContext().getDescription(), false));

        event.getChannel().sendMessageEmbeds(builder.build()).queue();


    }

    private String generateArguments(HelpContext context) {
        return context.getArguments().stream()
                .map(argument -> argument.isOptional() ? String.format("[<%s>] ", argument.getArgumentName()) : String.format("<%s> ", argument.getArgumentName()))
                .collect(Collectors.joining());
    }

}
