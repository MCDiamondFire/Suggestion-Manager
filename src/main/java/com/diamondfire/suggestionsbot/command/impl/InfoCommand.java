package com.diamondfire.suggestionsbot.command.impl;


import com.diamondfire.suggestionsbot.command.argument.ArgumentSet;
import com.diamondfire.suggestionsbot.command.help.*;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import net.dv8tion.jda.api.EmbedBuilder;

public class InfoCommand extends AbstractSuggestionCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Gets current open references to a specific message.")
                .addArgument(new HelpContextArgument()
                        .name("Suggestion Msg ID")
                );
    }

    @Override
    public Permission getPermission() {
        return Permission.MOD;
    }

    @Override
    public void run(CommandEvent event, Suggestion suggestion) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.addField("References: ", String.join("\n", suggestion.getReferenceManager().getReferences().keySet()), true);

        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }
}
