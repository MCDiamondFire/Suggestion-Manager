package com.diamondfire.suggestionsbot.command.impl;

import com.diamondfire.suggestionsbot.command.arguments.Argument;
import com.diamondfire.suggestionsbot.command.arguments.NoArg;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.database.SimpleSingleQuery;
import com.diamondfire.suggestionsbot.database.SingleQueryBuilder;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class RefreshCommand extends Command {

    @Override
    public String getName() {
        return "refresh";
    }

    @Override
    public String getDescription() {
        return "Forces the bot to refresh and examine all suggestions that are currently stored in the database." +
                "\nSuggestions which have their main suggestion deleted are permanently lost, and are deleted." +
                "\nReferences that are required are regenerated.";
    }

    @Override
    public Argument getArgument() {
        return new NoArg();
    }

    @Override
    public Permission getPermission() {
        return Permission.MOD;
    }

    @Override
    public void run(CommandEvent event) {
        new Thread(() -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Refreshing...");
            Message message = event.getChannel().sendMessage(builder.build()).complete();

                new SingleQueryBuilder().query("SELECT * from suggestions")
                        .onQuery((table) -> {
                            int deleted = 0;
                            int refreshed = 0;
                            do {
                                long suggestionID = table.getLong("message");
                                long suggestionChannel = table.getLong("message_channel");

                                Message suggestionMsg = null;
                                try {
                                    suggestionMsg = BotInstance.getJda().getTextChannelById(suggestionChannel).retrieveMessageById(suggestionID).complete(true);
                                } catch (Exception ignored) {
                                    ignored.printStackTrace();
                                }

                                if (suggestionMsg == null) {
                                    new SimpleSingleQuery().query("DELETE from suggestions WHERE message = ?", (statement -> {
                                        statement.setLong(1, suggestionID);
                                    })).execute();
                                    deleted++;
                                    continue;
                                }

                                Suggestion suggestion = new Suggestion(suggestionMsg);
                                suggestion.referenceManager.refreshReferences();
                                suggestion.databaseManager.refreshDBEntry();

                                refreshed++;
                            } while (table.next());

                            builder.setTitle("Refreshed Database!");
                            builder.setDescription(String.format("%s suggestions have been refreshed and %s have been deleted.", refreshed, deleted));

                            message.editMessage(builder.build()).queue();
                        }).execute();
        }).start();
    }
}
