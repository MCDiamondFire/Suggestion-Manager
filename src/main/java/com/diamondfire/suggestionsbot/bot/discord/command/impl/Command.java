package com.diamondfire.suggestionsbot.bot.discord.command.impl;

import com.diamondfire.suggestionsbot.bot.discord.command.argument.ArgumentSet;
import com.diamondfire.suggestionsbot.bot.discord.command.help.HelpContext;
import com.diamondfire.suggestionsbot.bot.discord.command.permissions.Permission;
import com.diamondfire.suggestionsbot.bot.discord.events.CommandEvent;

public abstract class Command {
    
    private ArgumentSet set = null;
    
    public abstract String getName();
    
    public boolean cacheArgumentSet() {
        return true;
    }
    
    public String[] getAliases() {
        return new String[0];
    }
    
    public abstract HelpContext getHelpContext();
    
    protected abstract ArgumentSet compileArguments();
    
    public ArgumentSet getArguments() {
        if (cacheArgumentSet()) {
            if (set == null) {
                set = compileArguments();
            }
            return set;
        } else {
            return compileArguments();
        }
    }
    
    public abstract Permission getPermission();
    
    public abstract void run(CommandEvent event);
}
