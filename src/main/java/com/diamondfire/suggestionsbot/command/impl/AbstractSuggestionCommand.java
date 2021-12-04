package com.diamondfire.suggestionsbot.command.impl;


import com.diamondfire.suggestionsbot.database.SingleQueryBuilder;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class AbstractSuggestionCommand extends BotCommand {

    @Override
    public CommandData createCommand() {
        return new CommandData(getName(), getDescription())
                .addOption(OptionType.STRING, "msg", "Suggestion Msg ID", true);
    }

    @Override
    public void run(SlashCommandEvent event) {
        long messageID = event.getOption("msg").getAsLong();
        new SingleQueryBuilder().query("SELECT * from suggestions WHERE message = ?", (statement) -> {
            statement.setLong(1, messageID);
        }).onQuery((set) -> {
            long channel = set.getLong("message_channel");
            Suggestion suggestion = new Suggestion(BotInstance.getJda().getTextChannelById(channel).retrieveMessageById(messageID).complete());
            run(event, suggestion);
        }).onNotFound(() -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Cannot find suggestion!");
            event.replyEmbeds(builder.build()).queue();
        }).execute();

    }

    public abstract void run(SlashCommandEvent event, Suggestion suggestion);
}
