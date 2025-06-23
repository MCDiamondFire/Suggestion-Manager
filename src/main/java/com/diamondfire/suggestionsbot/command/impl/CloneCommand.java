package com.diamondfire.suggestionsbot.command.impl;


import com.diamondfire.suggestionsbot.command.argument.ArgumentSet;
import com.diamondfire.suggestionsbot.command.argument.impl.types.LongArgument;
import com.diamondfire.suggestionsbot.command.help.HelpContext;
import com.diamondfire.suggestionsbot.command.help.HelpContextArgument;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.events.CommandEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CloneCommand extends Command {

    @Override
    public String getName() {
        return "clone";
    }

    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Clones a specific channels contents to #guidelines.")
                .addArgument(new HelpContextArgument()
                        .name("Channel ID to clone")
                );
    }

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet().addArgument("id",
                new LongArgument());
    }

    @Override
    public Permission getPermission() {
        return Permission.MOD;
    }

    @Override
    public void run(CommandEvent event) {
        TextChannel channelToClone = event.getGuild().getTextChannelById(event.getArgument("id"));
        TextChannel toChannel = event.getGuild().getTextChannelsByName("guidelines", true).getFirst();
        if (channelToClone == null) {
            return;
        }
        List<Message> toPurge = MessageHistory.getHistoryFromBeginning(channelToClone).limit(100).complete().getRetrievedHistory();

        MessageHistory.getHistoryFromBeginning(channelToClone).limit(100).queue(msgs -> {
            List<Message> history = new ArrayList<>(msgs.getRetrievedHistory());
            Collections.reverse(history);
            for (Message msg : history) {
                msg.forwardTo(toChannel).queue();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        new Thread(() -> {
            for (CompletableFuture<?> future : toChannel.purgeMessages(toPurge)) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

}
