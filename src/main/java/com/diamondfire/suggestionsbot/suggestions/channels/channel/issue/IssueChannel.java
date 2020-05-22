package com.diamondfire.suggestionsbot.suggestions.channels.channel.issue;

import com.diamondfire.suggestionsbot.suggestions.channels.Channel;
import net.dv8tion.jda.api.entities.Emote;

public abstract class IssueChannel extends Channel {

    @Override
    public boolean canGoPopular() {
        return false;
    }

    @Override
    protected Emote[] getEmotes() {
        return new Emote[0];
    }
}
