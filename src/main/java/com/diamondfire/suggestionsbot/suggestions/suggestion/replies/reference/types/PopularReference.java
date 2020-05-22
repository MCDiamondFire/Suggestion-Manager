package com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types;


import com.diamondfire.suggestionsbot.suggestions.suggestion.ReactionManager;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.Reference;
import com.diamondfire.suggestionsbot.suggestions.reactions.PopularHandler;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.util.BotConstants;
import com.diamondfire.suggestionsbot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class PopularReference extends Reference {


    @Override
    public String getName() {
        return "popular_message";
    }

    @Override
    public long getChannelID() {
        return BotConstants.REACTION_POPULAR;
    }

    @Override
    public boolean syncEmojis() {
        return true;
    }

    @Override
    public boolean mustExist() {
        return true;
    }

    @Override
    public MessageEmbed create(Suggestion suggestion) {
        PopularHandler.calculate();

        Message message = suggestion.getSuggestion();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(message.getAuthor().getName(), null, message.getAuthor().getEffectiveAvatarUrl());

        StringBuilder description = new StringBuilder();
        description.append(String.format("\uD83D\uDCE8 Jump to [%s](" + message.getJumpUrl() + ")", suggestion.getChannel().getName()));
        //TODO Fix issue where if the popular message isn't there, it doesn't correctly fetches the discussion message.
        // The reason for this, is quite simple.
        if (suggestion.referenceManager != null) {
            Reference reference = suggestion.referenceManager.getReference("discussion_message");
            if (reference != null) {
                description.append(" or [Discussion](" + reference.getReference().getJumpUrl() + ")");
            }
        }

        builder.setDescription(description.toString());
        builder.addField("\u200b", Util.trim(message.getContentRaw(), 256), false);


        ReactionManager manager = suggestion.reactionManager;

        Reaction reaction = manager.getTopReaction();
        Color color = reaction != null ? reaction.getColor() : Color.gray;

        builder.setColor(color);


        builder.setFooter("+" + manager.getNetVotes() + " (" + manager.getUpVotes() + "|" + manager.getDownVotes() + ")");

        return builder.build();

    }
}
