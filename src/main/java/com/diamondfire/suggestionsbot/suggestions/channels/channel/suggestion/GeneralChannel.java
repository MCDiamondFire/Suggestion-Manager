package com.diamondfire.suggestionsbot.suggestions.channels.channel.suggestion;

import com.diamondfire.suggestionsbot.SuggestionsBot;

public class GeneralChannel extends SuggestionChannel {
    @Override
    public String getName() {
        return "General Suggestion";
    }

    @Override
    public long getID() {
        return SuggestionsBot.config.GENERAL_SUGGESTIONS_CHANNEL;
    }
}
