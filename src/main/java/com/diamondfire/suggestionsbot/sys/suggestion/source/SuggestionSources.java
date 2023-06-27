package com.diamondfire.suggestionsbot.sys.suggestion.source;

import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.DiscordSuggestionSource;

public interface SuggestionSources {
    
    DiscordSuggestionSource DISCORD = new DiscordSuggestionSource();
    
}
