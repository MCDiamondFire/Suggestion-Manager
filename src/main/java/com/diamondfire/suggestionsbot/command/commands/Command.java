package com.diamondfire.suggestionsbot.command.commands;


import com.diamondfire.suggestionsbot.command.arguments.Argument;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.events.CommandEvent;

public abstract class Command {
    public abstract String getName();

    public abstract String getDescription();

    public abstract Argument getArgument();

    public abstract Permission getPermission();

    protected boolean inHelp() {
        return true;
    }

    public abstract void run(CommandEvent event);
}
