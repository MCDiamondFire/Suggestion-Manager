package com.diamondfire.suggestionsbot.command.impl;


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
    public String getDescription() {
        return "Forces a message to become popular.";
    }

    @Override
    public Permission getPermission() {
        return Permission.MOD;
    }

    @Override
    public void run(CommandEvent event, Suggestion suggestion) {
        suggestion.referenceManager.newReference(new PopularReference());
    }
}
