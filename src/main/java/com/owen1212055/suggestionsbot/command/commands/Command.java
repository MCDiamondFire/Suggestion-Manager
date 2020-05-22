package com.owen1212055.suggestionsbot.command.commands;


import com.owen1212055.suggestionsbot.command.arguments.Argument;
import com.owen1212055.suggestionsbot.command.permissions.Permission;
import com.owen1212055.suggestionsbot.events.CommandEvent;

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
