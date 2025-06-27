package com.diamondfire.suggestionsbot.command.impl;


import com.diamondfire.suggestionsbot.command.BotCommand;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.discord.jda5.JDAInteraction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CloneCommand implements BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloneCommand.class);

    @Override
    public Permission getPermission() {
        return Permission.MOD;
    }

    @Command("clone <id>")
    public void command(final @NotNull JDAInteraction interaction, @Argument("id") TextChannel channelToClone) {
        Guild guild = interaction.guild();
        if (guild == null) {
            return;
        }
        TextChannel toChannel = guild.getTextChannelsByName("guidelines", true).getFirst();
        if (channelToClone == null) {
            return;
        }
        List<Message> toPurge = MessageHistory.getHistoryFromBeginning(channelToClone).limit(100).complete().getRetrievedHistory();

        MessageHistory.getHistoryFromBeginning(channelToClone).limit(100).queue(messageHistory -> {
            List<Message> history = new ArrayList<>(messageHistory.getRetrievedHistory());
            Collections.reverse(history);
            for (Message msg : history) {
                msg.forwardTo(toChannel).queue();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error("Exception thrown while purging history", e);
                }
            }

        });

        new Thread(() -> {
            for (CompletableFuture<?> future : toChannel.purgeMessages(toPurge)) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.error("Exception thrown while purging history", e);
                }
            }
        }).start();
    }

}
