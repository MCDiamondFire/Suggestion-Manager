package com.diamondfire.suggestionsbot.suggestions.channels.channel.issue;

import com.diamondfire.suggestionsbot.SuggestionsBot;

public class ProductionIssue extends IssueChannel {
    @Override
    public String getName() {
        return "Issue";
    }

    @Override
    public long getID() {
        return SuggestionsBot.config.ISSUES_CHANNEL;
    }
}
