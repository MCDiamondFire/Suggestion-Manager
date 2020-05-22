package com.owen1212055.suggestionsbot.command.commands;

import com.owen1212055.suggestionsbot.command.arguments.Argument;
import com.owen1212055.suggestionsbot.command.arguments.LazyStringArg;
import com.owen1212055.suggestionsbot.command.permissions.Permission;
import com.owen1212055.suggestionsbot.command.permissions.PermissionHandler;
import com.owen1212055.suggestionsbot.events.CommandEvent;
import com.owen1212055.suggestionsbot.instance.BotInstance;
import com.owen1212055.suggestionsbot.util.BotConstants;
import net.dv8tion.jda.api.EmbedBuilder;


public class HelpCommand extends Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows all commands";
    }

    @Override
    public Argument getArgument() {
        return new LazyStringArg();
    }

    @Override
    public Permission getPermission() {
        return Permission.USER;
    }

    @Override
    public void run(CommandEvent event) {
        if (event.getArguments().length == 0) {
            EmbedBuilder builder = new EmbedBuilder();

            builder.setTitle("Help");
            builder.setDescription("E");
            builder.setThumbnail(BotInstance.getJda().getSelfUser().getAvatarUrl());
            builder.setFooter("Your permissions: " + PermissionHandler.getPermission(event.getMember()));

            BotInstance.getHandler().getCommands().values().stream()
                    .filter(Command::inHelp)
                    .filter((command) -> command.getPermission().hasPermission(event.getMember()))
                    .forEach(command -> builder.addField(BotConstants.PREFIX + command.getName() + " " + command.getArgument(), command.getDescription(), false));

            event.getChannel().sendMessage(builder.build()).queue();
            return;
        }


    }

}
