package com.diamondfire.suggestionsbot.command.commands;

import com.diamondfire.suggestionsbot.command.arguments.Argument;
import com.diamondfire.suggestionsbot.command.arguments.NoArg;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.command.permissions.PermissionHandler;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.util.BotConstants;
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
        return new NoArg();
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
                .filter(Command::inHelp)
                .filter((command) -> command.getPermission().hasPermission(event.getMember()))
                .forEach(command -> builder.addField(BotConstants.PREFIX + command.getName() + " " + command.getArgument().getName(), command.getDescription(), false));

        event.getChannel().sendMessage(builder.build()).queue();


    }

}
