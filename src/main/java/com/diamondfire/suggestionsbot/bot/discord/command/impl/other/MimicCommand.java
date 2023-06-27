package com.diamondfire.suggestionsbot.bot.discord.command.impl.other;

import com.diamondfire.suggestionsbot.bot.discord.command.argument.ArgumentSet;
import com.diamondfire.suggestionsbot.bot.discord.command.argument.impl.parsing.types.MessageArgument;
import com.diamondfire.suggestionsbot.bot.discord.command.help.*;
import com.diamondfire.suggestionsbot.bot.discord.command.impl.Command;
import com.diamondfire.suggestionsbot.bot.discord.command.permissions.Permission;
import com.diamondfire.suggestionsbot.bot.discord.events.CommandEvent;


public class MimicCommand extends Command {
    
    @Override
    public String getName() {
        return "mimic";
    }
    
    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Removes your message and replaces it with its own.")
                .category(CommandCategory.OTHER)
                .addArgument(
                        new HelpContextArgument().name("message")
                );
    }
    
    @Override
    public ArgumentSet compileArguments() {
        return new ArgumentSet()
                .addArgument("msg", new MessageArgument());
    }
    
    @Override
    public Permission getPermission() {
        return Permission.BOT_DEVELOPER;
    }
    
    @Override
    public void run(CommandEvent event) {
        String msg = event.getArgument("msg");
        
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(msg).queue();
    }
    
}
