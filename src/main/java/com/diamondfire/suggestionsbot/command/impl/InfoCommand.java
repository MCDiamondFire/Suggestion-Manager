package com.diamondfire.suggestionsbot.command.impl;


import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.command.permissions.Permissions;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class InfoCommand extends AbstractSuggestionCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Gets current open references to a specific message.";
    }

    @Override
    public Permission getPermission() {
        return Permissions.TESTER;
    }

    @Override
    public void run(SlashCommandEvent event, Suggestion suggestion) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.addField("References: ", String.join("\n", suggestion.referenceManager.getReferences().keySet()), true);

        event.replyEmbeds(builder.build()).queue();
    }
}
