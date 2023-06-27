package com.diamondfire.suggestionsbot.bot.discord.command;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.bot.discord.command.disable.DisableCommandHandler;
import com.diamondfire.suggestionsbot.bot.discord.command.executor.CommandExecutor;
import com.diamondfire.suggestionsbot.bot.discord.command.impl.Command;
import com.diamondfire.suggestionsbot.bot.discord.events.CommandEvent;

import java.util.HashMap;

public class CommandHandler {
    
    private final HashMap<String, Command> CMDS = new HashMap<>();
    private final HashMap<String, Command> ALIASES = new HashMap<>();
    private final CommandExecutor COMMAND_EXECUTOR = new CommandExecutor();
    private final DisableCommandHandler DISABLED_COMMAND_HANDLER = new DisableCommandHandler();
    
    public void initialize() {
        DISABLED_COMMAND_HANDLER.initialize();
    }
    
    public static Command getCommand(String name) {
        Command cmd = DiscordInstance.getHandler().getCommands().get(name.toLowerCase());
        if (cmd == null) {
            cmd = DiscordInstance.getHandler().getAliases().get(name.toLowerCase());
        }
        
        return cmd;
    }
    
    public void register(Command... commands) {
        for (Command command : commands) {
            this.CMDS.put(command.getName().toLowerCase(), command);
            for (String alias : command.getAliases()) {
                this.ALIASES.put(alias.toLowerCase(), command);
            }
        }
        
    }
    
    public void run(CommandEvent e) {
        COMMAND_EXECUTOR.run(e);
    }
    
    public HashMap<String, Command> getCommands() {
        return CMDS;
    }
    
    public HashMap<String, Command> getAliases() {
        return ALIASES;
    }
    
    public DisableCommandHandler getDisabledHandler() {
        return DISABLED_COMMAND_HANDLER;
    }
}
