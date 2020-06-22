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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
            AtomicInteger deleted = new AtomicInteger();
            AtomicInteger refreshed = new AtomicInteger();
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Refreshing...");
            Map<Long, Long> suggestions = new HashMap<>();
            event.getChannel().sendMessage(builder.build()).queue();

                new SingleQueryBuilder().query("SELECT * from suggestions")
                        .onQuery((table) -> {
                            do {
                                suggestions.put(table.getLong("message"), table.getLong("message_channel"));
                            } while (table.next());

                            for (Map.Entry<Long, Long> entry : suggestions.entrySet()) {
                                long suggestionID = entry.getKey();
                                long suggestionChannel = entry.getValue();

                                BotInstance.getJda().getTextChannelById(suggestionChannel).retrieveMessageById(suggestionID).queue((msg) -> {
                                    System.out.println("refreshing " + msg);
                                    Suggestion suggestion = new Suggestion(msg);
                                    suggestion.referenceManager.fetchReferences().thenRun(suggestion.referenceManager::refreshReferences);
                                    suggestion.databaseManager.refreshDBEntry();
                                    refreshed.getAndIncrement();
                                }, (throwable) -> {
//                                    new SimpleSingleQuery().query("DELETE from suggestions WHERE message = ?", (statement -> {
//                                        statement.setLong(1, suggestionID);
//                                    })).execute();
                                    System.out.println("deleting " + suggestionID);
                                    deleted.getAndIncrement();
                                });

                            }

                        }).execute();

        }).start();
    }
}
