package com.diamondfire.suggestionsbot.command.impl;

import com.diamondfire.suggestionsbot.command.help.HelpContext;
import com.diamondfire.suggestionsbot.command.help.HelpContextArgument;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types.PopularReference;

public class PopularCommand extends AbstractSuggestionCommand {

    @Override
    public String getName() {
        return "popular";
    }

    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Forces a message to become popular.")
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
        suggestion.getReferenceManager().newReference(new PopularReference(event.getGuild().getId()));
    }

}
