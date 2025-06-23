package com.diamondfire.suggestionsbot.suggestions.channels.channel.suggestion;

import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.channels.Channel;
import com.diamondfire.suggestionsbot.util.BotConstants;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;

public abstract class SuggestionChannel extends Channel {

    @Override
    public boolean canGoPopular() {
        return true;
    }

    @Override
    protected RichCustomEmoji[] getEmotes() {
        return new RichCustomEmoji[]{BotInstance.getJda().getEmojiById(BotConstants.UPVOTE),
                BotInstance.getJda().getEmojiById(BotConstants.DOWNVOTE)};
    }

}
