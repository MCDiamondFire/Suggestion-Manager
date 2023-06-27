package com.diamondfire.suggestionsbot.bot.discord.command.impl.other;

import com.diamondfire.suggestionsbot.bot.discord.command.argument.ArgumentSet;
import com.diamondfire.suggestionsbot.bot.discord.command.help.*;
import com.diamondfire.suggestionsbot.bot.discord.command.impl.Command;
import com.diamondfire.suggestionsbot.bot.discord.command.permissions.Permission;
import com.diamondfire.suggestionsbot.bot.discord.events.CommandEvent;
import com.diamondfire.suggestionsbot.bot.discord.restart.RestartHandler;
import net.dv8tion.jda.api.EmbedBuilder;

public class RestartCommand extends Command {
    
    @Override
    public String getName() {
        return "restart";
    }
    
    @Override
    public String[] getAliases() {
        return new String[]{"stop", "exit"};
    }
    
    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Restarts the bot.")
                .category(CommandCategory.OTHER);
    }
    
    @Override
    public ArgumentSet compileArguments() {
        return new ArgumentSet();
    }
    
    @Override
    public Permission getPermission() {
        return Permission.BOT_DEVELOPER;
    }
    
    @Override
    public void run(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Restarting!");
        builder.setDescription("This may take a moment");
        
        event.getChannel().sendMessage(builder.build()).queue((msg) -> {
            RestartHandler.logRestart(msg);
            System.exit(0);
        });
        
    }
    
}


