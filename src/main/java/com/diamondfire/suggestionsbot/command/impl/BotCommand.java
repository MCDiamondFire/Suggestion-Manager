package com.diamondfire.suggestionsbot.command.impl;


import com.diamondfire.suggestionsbot.command.permissions.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class BotCommand {

    public abstract String getName();
    public abstract String getDescription();
    public abstract CommandData createCommand();
    public abstract Permission getPermission();
    public abstract void run(SlashCommandEvent event);
}
