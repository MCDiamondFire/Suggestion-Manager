package com.diamondfire.suggestionsbot.command.impl;


import com.diamondfire.suggestionsbot.command.argument.ArgumentSet;
import com.diamondfire.suggestionsbot.command.argument.impl.types.LongArgument;

import com.diamondfire.suggestionsbot.database.SingleQueryBuilder;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import net.dv8tion.jda.api.EmbedBuilder;

public abstract class AbstractSuggestionCommand extends Command {

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet().addArgument("msg",
                new LongArgument());
    }

    @Override
    public void run(CommandEvent event) {
        long messageID = event.getArgument("msg");
        new SingleQueryBuilder().query("SELECT * from suggestions WHERE message = ?", (statement) -> {
            statement.setLong(1, messageID);
        }).onQuery((set) -> {
            long channel = set.getLong("message_channel");
            Suggestion suggestion = new Suggestion(BotInstance.getJda().getTextChannelById(channel).retrieveMessageById(messageID).complete());
            run(event, suggestion);
        }).onNotFound(() -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Cannot find suggestion!");
            event.getChannel().sendMessage(builder.build()).queue();
        }).execute();

    }

    public abstract void run(CommandEvent event, Suggestion suggestion);
}
