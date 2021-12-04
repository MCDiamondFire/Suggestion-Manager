package com.diamondfire.suggestionsbot.suggestions.channels.channel.issue;

import com.diamondfire.suggestionsbot.SuggestionsBot;

public class BetaIssue extends IssueChannel {
    @Override
    public String getName() {
        return "Beta Issue";
    }

    @Override
    public long getID() {
        return SuggestionsBot.config.BETA_ISSUES_CHANNEL;
    }
}
