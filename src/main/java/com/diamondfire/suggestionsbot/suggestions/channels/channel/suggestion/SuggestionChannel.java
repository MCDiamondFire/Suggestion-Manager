package com.diamondfire.suggestionsbot.suggestions.channels.channel.suggestion;

import com.diamondfire.suggestionsbot.util.BotConstants;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.channels.Channel;
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
