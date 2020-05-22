package com.owen1212055.suggestionsbot.command.commands;


import com.owen1212055.suggestionsbot.command.permissions.Permission;
import com.owen1212055.suggestionsbot.events.CommandEvent;
import com.owen1212055.suggestionsbot.suggestions.suggestion.Suggestion;
import net.dv8tion.jda.api.EmbedBuilder;

public class InfoCommand extends AbstractSuggestionCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Gets stats on a specific message";
    }

    @Override
    public Permission getPermission() {
        return Permission.MOD;
    }


    @Override
    public void run(CommandEvent event, Suggestion suggestion) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.addField("References: ", String.join("\n", suggestion.referenceManager.getReferences().keySet()), true);


        //builder.addField("Suggestion Stats", String.join("\n", stats), true);

        event.getChannel().sendMessage(builder.build()).queue();
    }
}
