package com.owen1212055.suggestionsbot.suggestions.channels;

import com.owen1212055.suggestionsbot.suggestions.channels.channel.issue.BetaIssue;
import com.owen1212055.suggestionsbot.suggestions.channels.channel.issue.ProductionIssue;
import com.owen1212055.suggestionsbot.suggestions.channels.channel.suggestion.CodeChannel;
import com.owen1212055.suggestionsbot.suggestions.channels.channel.suggestion.GeneralChannel;

import java.util.HashMap;

public class ChannelHandler {
    private static final HashMap<Long, Channel> channelRegistry = new HashMap<>();

    static {
        register(new GeneralChannel());
        register(new CodeChannel());
        register(new ProductionIssue());
        register(new BetaIssue());
    }

    private static void register(Channel channel) {
        channelRegistry.put(channel.getID(), channel);
    }

    public static Channel getChannel(long id) {
        return channelRegistry.get(id);
    }

    public static boolean isValidChannel(long id) {
        return channelRegistry.containsKey(id);
    }
}
