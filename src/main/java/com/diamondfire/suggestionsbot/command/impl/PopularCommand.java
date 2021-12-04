package com.diamondfire.suggestionsbot.command.impl;


import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.command.permissions.Permissions;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types.PopularReference;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class PopularCommand extends AbstractSuggestionCommand {
    @Override
    public String getName() {
        return "popular";
    }

    @Override
    public String getDescription() {
        return "Forces a suggestion to become popular.";
    }

    @Override
    public Permission getPermission() {
        return Permissions.MODERATOR;
    }

    @Override
    public void run(SlashCommandEvent event, Suggestion suggestion) {
        suggestion.referenceManager.newReference(new PopularReference());
    }
}
