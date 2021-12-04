package com.diamondfire.suggestionsbot.events;

import com.diamondfire.suggestionsbot.suggestions.channels.ChannelHandler;
import com.diamondfire.suggestionsbot.util.MessageAcceptor;
import com.diamondfire.suggestionsbot.util.chatfilter.FilterAcceptor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class MessageEvent extends ListenerAdapter {
    private static final MessageAcceptor[] acceptors = {
            new FilterAcceptor()
    };

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        for (MessageAcceptor acceptor : acceptors) {
            if (acceptor.accept(message)) {
                break;
            }
        }

        long channelID = event.getChannel().getIdLong();

        if (ChannelHandler.isValidChannel(channelID)) {
            if (!event.getMember().getRoles().contains(event.getGuild().getRoleById(530860699937669130L))) {
                ChannelHandler.getChannel(channelID).onMessage(message);
            } else {
                event.getAuthor().openPrivateChannel().queue((privateChannel -> {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("Notice!");
                    builder.setDescription("You are unable to suggest at this time. If you are curious as to why that is the case, please contact a moderator. I am sending you your suggestion right now so you can resend it later.");
                    privateChannel.sendMessageEmbeds(builder.build()).queue();
                    privateChannel.sendMessage(event.getMessage()).queue();

                }));
                event.getMessage().delete().reason("User has no-suggest role!").queue();
            }
        }

    }
}
