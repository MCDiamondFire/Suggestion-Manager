package com.diamondfire.suggestionsbot.command.commands;


import com.diamondfire.suggestionsbot.command.arguments.Argument;
import com.diamondfire.suggestionsbot.command.arguments.value.LongArg;
import com.diamondfire.suggestionsbot.command.arguments.value.ValueArgument;
import com.diamondfire.suggestionsbot.database.SingleQueryBuilder;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.channels.Channel;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractSuggestionCommand extends Command {
    @Override
    public ValueArgument<Long> getArgument() {
        return new LongArg("Suggestion ID", true);
    }

    @Override
    public void run(CommandEvent event) {
        long messageID = getArgument().getArg(event.getParsedArgs());
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
