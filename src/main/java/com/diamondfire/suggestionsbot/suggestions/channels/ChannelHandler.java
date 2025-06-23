package com.diamondfire.suggestionsbot.suggestions.channels;

import com.diamondfire.suggestionsbot.util.config.ConfigLoader;
import com.diamondfire.suggestionsbot.util.config.type.ConfigSuggestionsChannel;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public final class ChannelHandler {

    private static final HashMap<Long, SuggestionsChannel> channelRegistry = new HashMap<>();

    static {
        for (ConfigSuggestionsChannel config : ConfigLoader.getConfig().getChannels()) {
            register(new SuggestionsChannel(config));
        }
    }

    private ChannelHandler() {
    }

    private static void register(SuggestionsChannel channel) {
        channelRegistry.put(channel.getId(), channel);
    }

    public static SuggestionsChannel getChannel(long id) {
        return channelRegistry.get(id);
    }

    public static @Nullable SuggestionsChannel getSuggestionsChannelOrNull(long id) {
        if (channelRegistry.containsKey(id) && channelRegistry.get(id) instanceof SuggestionsChannel channel) {
            return channel;
        }
        return null;
    }

}
