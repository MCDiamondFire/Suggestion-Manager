package com.diamondfire.suggestionsbot.command.impl;


import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.command.argument.ArgumentSet;
import com.diamondfire.suggestionsbot.command.argument.impl.types.LongArgument;
import com.diamondfire.suggestionsbot.database.SingleQueryBuilder;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public abstract class AbstractSuggestionCommand extends Command {

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet().addArgument("msg",
                new LongArgument());
    }

    @Override
    public void run(CommandEvent event) {
        long messageId = event.getArgument("msg");
        new SingleQueryBuilder().query("SELECT * from suggestions WHERE message = ?", statement ->
                statement.setLong(1, messageId)
        ).onQuery(set -> {
            long channel = set.getLong("message_channel");
            TextChannel textChannel = BotInstance.getJda().getTextChannelById(channel);
            if (textChannel == null) {
                return;
            }
            Suggestion suggestion = new Suggestion(textChannel.retrieveMessageById(messageId).complete());
            this.run(event, suggestion);
        }).onNotFound(() -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Cannot find suggestion!");
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
        }).execute();

    }

    public abstract void run(CommandEvent event, Suggestion suggestion);

}
