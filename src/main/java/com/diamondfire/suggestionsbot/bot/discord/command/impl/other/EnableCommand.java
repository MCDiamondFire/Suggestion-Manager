package com.diamondfire.suggestionsbot.bot.discord.command.impl.other;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.bot.discord.command.CommandHandler;
import com.diamondfire.suggestionsbot.bot.discord.command.argument.ArgumentSet;
import com.diamondfire.suggestionsbot.bot.discord.command.argument.impl.types.StringArgument;
import com.diamondfire.suggestionsbot.bot.discord.command.disable.*;
import com.diamondfire.suggestionsbot.bot.discord.command.help.*;
import com.diamondfire.suggestionsbot.bot.discord.command.impl.Command;
import com.diamondfire.suggestionsbot.bot.discord.command.permissions.Permission;
import com.diamondfire.suggestionsbot.bot.discord.command.reply.PresetBuilder;
import com.diamondfire.suggestionsbot.bot.discord.command.reply.feature.informative.*;
import com.diamondfire.suggestionsbot.bot.discord.events.CommandEvent;

public class EnableCommand extends Command implements CommandDisableFlag{
    
    @Override
    public String getName() {
        return "enable";
    }
    
    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("This command enables a disabled command.")
                .category(CommandCategory.OTHER)
                .addArgument(
                        new HelpContextArgument()
                                .name("cmd")
                );
    }
    
    @Override
    public ArgumentSet compileArguments() {
        return new ArgumentSet().addArgument("cmd",
                new StringArgument()
        );
    }
    
    @Override
    public Permission getPermission() {
        return Permission.ADMINISTRATOR;
    }
    
    @Override
    public void run(CommandEvent event) {
        DisableCommandHandler handler = DiscordInstance.getHandler().getDisabledHandler();
        PresetBuilder builder = new PresetBuilder();
        String name = event.getArgument("cmd");
        Command command = CommandHandler.getCommand(name);
        
        if (command != null) {
            if (!handler.isDisabled(command)) {
                builder.withPreset(new InformativeReply(InformativeReplyType.ERROR, "Command isn't disabled!"));
            } else {
                handler.enable(command);
                builder.withPreset(new InformativeReply(InformativeReplyType.SUCCESS, String.format("Command ``%s`` has been enabled.", command.getName())));
            }
        } else {
            builder.withPreset(new InformativeReply(InformativeReplyType.ERROR, String.format("Command ``%s`` could not be found.", name)));
        }
        
        event.reply(builder);
    }
    
}


