package com.diamondfire.suggestionsbot.suggestions.channels.channel.suggestion;

import com.diamondfire.suggestionsbot.SuggestionsBot;

public class CodeChannel extends SuggestionChannel {
    @Override
    public String getName() {
        return "Code Suggestion";
    }

    @Override
    public long getID() {
        return SuggestionsBot.config.CODE_SUGGESTIONS_CHANNEL;
    }
}
