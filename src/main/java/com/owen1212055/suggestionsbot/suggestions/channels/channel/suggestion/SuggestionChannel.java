package com.owen1212055.suggestionsbot.suggestions.channels.channel.suggestion;

import com.owen1212055.suggestionsbot.instance.BotInstance;
import com.owen1212055.suggestionsbot.suggestions.channels.Channel;
import com.owen1212055.suggestionsbot.util.BotConstants;
import net.dv8tion.jda.api.entities.Emote;

public abstract class SuggestionChannel extends Channel {

    @Override
    public boolean canGoPopular() {
        return true;
    }

    @Override
    protected Emote[] getEmotes() {
        return new Emote[]{BotInstance.getJda().getEmoteById(BotConstants.UPVOTE),
                BotInstance.getJda().getEmoteById(BotConstants.DOWNVOTE)};
    }
}
