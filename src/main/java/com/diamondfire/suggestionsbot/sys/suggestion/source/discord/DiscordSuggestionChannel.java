package com.diamondfire.suggestionsbot.sys.suggestion.source.discord;

import com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.SuggestionType;

public enum DiscordSuggestionChannel {
    
    GENERAL_SUGGESTION(829821713503092736L, SuggestionType.GENERAL_SUGGESTION),
    CODE_SUGGESTION(829821726635982955L, SuggestionType.CODE_SUGGESTION),
    ISSUE(829821750220292126L, SuggestionType.ISSUE),
    BETA_ISSUE(829821739336597504L, SuggestionType.BETA_ISSUE),
    ;
    
    private final long channelId;
    private final SuggestionType type;
    
    DiscordSuggestionChannel(long channelId, SuggestionType type) {
        this.channelId = channelId;
        this.type = type;
    }
    
    public static DiscordSuggestionChannel getChannel(SuggestionType type) {
        DiscordSuggestionChannel channel = null;
        for (DiscordSuggestionChannel discordSuggestionChannel : DiscordSuggestionChannel.values()) {
            if (discordSuggestionChannel.getType() == type) {
                channel = discordSuggestionChannel;
            }
        }
        
        return channel;
    }
    
    public static DiscordSuggestionChannel getChannel(long id) {
        DiscordSuggestionChannel channel = null;
        for (DiscordSuggestionChannel discordSuggestionChannel : DiscordSuggestionChannel.values()) {
            if (discordSuggestionChannel.getChannelId() == id) {
                channel = discordSuggestionChannel;
            }
        }
        
        return channel;
    }
    
    public long getChannelId() {
        return channelId;
    }
    
    public SuggestionType getType() {
        return type;
    }
    
}
