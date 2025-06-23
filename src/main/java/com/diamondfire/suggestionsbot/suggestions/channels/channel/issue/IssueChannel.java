package com.diamondfire.suggestionsbot.suggestions.channels.channel.issue;

import com.diamondfire.suggestionsbot.suggestions.channels.Channel;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;

public abstract class IssueChannel extends Channel {

    @Override
    public boolean canGoPopular() {
        return false;
    }

    @Override
    protected RichCustomEmoji[] getEmotes() {
        return new RichCustomEmoji[0];
    }

}
