package com.diamondfire.suggestionsbot.events;

import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.channels.ChannelHandler;
import com.diamondfire.suggestionsbot.suggestions.channels.SuggestionsChannel;
import com.diamondfire.suggestionsbot.util.BotConstants;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        Message message = event.getMessage();
        long channelId = event.getChannel().getIdLong();

        SuggestionsChannel channel = ChannelHandler.getSuggestionsChannelOrNull(channelId);
        if (channel != null) {
            channel.onMessage(message);
        }

    }

}
