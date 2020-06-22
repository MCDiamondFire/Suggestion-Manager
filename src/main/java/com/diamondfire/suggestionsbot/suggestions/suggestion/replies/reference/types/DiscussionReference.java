package com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types;

import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.Reference;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.util.BotConstants;
import com.diamondfire.suggestionsbot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.awt.*;

public class DiscussionReference extends Reference {

    @Override
    public String getName() {
        return "discussion_message";
    }

    @Override
    public long getChannelID() {
        return BotConstants.DISCUSSION;
    }

    @Override
    public boolean syncEmojis() {
        return true;
    }

    @Override
    public boolean mustExist() {
        return false;
    }

    @Override
    public MessageEmbed create(Suggestion suggestion) {
        EmbedBuilder builder = new EmbedBuilder();
        Message message = suggestion.getSuggestion();
        Reaction reaction = suggestion.reactionManager.getTopReaction();
        Member member = message.getGuild().retrieveMember(message.getAuthor()).complete();

        builder.setAuthor(member.getEffectiveName(), null, message.getAuthor().getEffectiveAvatarUrl());
        builder.setColor(reaction != null ? reaction.getColor() : Color.gray);
        builder.setDescription(String.format("\uD83D\uDCE8 [New %s posted](" + message.getJumpUrl() + ")", suggestion.getChannel().getName().toLowerCase()));
        builder.addField("\u200b", Util.trim(message.getContentRaw(), 256), false);
        builder.setFooter("Posted in #" + message.getChannel().getName());
        return builder.build();
    }
}
