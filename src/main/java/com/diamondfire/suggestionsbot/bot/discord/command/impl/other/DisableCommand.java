package com.diamondfire.suggestionsbot.bot.discord.command.impl.other;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.bot.discord.command.CommandHandler;
import com.diamondfire.suggestionsbot.bot.discord.command.argument.ArgumentSet;
import com.diamondfire.suggestionsbot.bot.discord.command.argument.impl.types.StringArgument;
import com.diamondfire.suggestionsbot.bot.discord.command.disable.CommandDisableFlag;
import com.diamondfire.suggestionsbot.bot.discord.command.help.*;
import com.diamondfire.suggestionsbot.bot.discord.command.impl.Command;
import com.diamondfire.suggestionsbot.bot.discord.command.permissions.Permission;
import com.diamondfire.suggestionsbot.bot.discord.command.reply.PresetBuilder;
import com.diamondfire.suggestionsbot.bot.discord.command.reply.feature.informative.*;
import com.diamondfire.suggestionsbot.bot.discord.events.CommandEvent;

public class DisableCommand extends Command implements CommandDisableFlag {
    
    @Override
    public String getName() {
        return "disable";
    }
    
    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("This command disables a command entirely.")
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
        PresetBuilder builder = new PresetBuilder();
        String name = event.getArgument("cmd");
        Command command = CommandHandler.getCommand(name);
        if (command instanceof CommandDisableFlag) {
            builder.withPreset(new InformativeReply(InformativeReplyType.ERROR, "You cannot disable these commands!"));
            event.reply(builder);
            return;
        }
        
        if (command != null) {
            DiscordInstance.getHandler().getDisabledHandler().disable(command);
            builder.withPreset(new InformativeReply(InformativeReplyType.SUCCESS, String.format("Command ``%s`` has been disabled.", command.getName())));
        } else {
            builder.withPreset(new InformativeReply(InformativeReplyType.ERROR, String.format("Command ``%s`` could not be found.", name)));
        }
        
        event.reply(builder);
    }
    
}


