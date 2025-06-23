package com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types;

import com.diamondfire.suggestionsbot.suggestions.reactions.ResultReaction;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.Reference;
import com.diamondfire.suggestionsbot.util.Util;
import com.diamondfire.suggestionsbot.util.config.ConfigLoader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;

public class DiscussionReference extends Reference {

    private final long channelId;

    public DiscussionReference(String guildId) {
        this.channelId = ConfigLoader.getConfig().getGuilds().get(guildId).getDiscussionChannel();
    }

    @Override
    public String getName() {
        return "discussion_message";
    }

    @Override
    public long getChannelId() {
        return this.channelId;
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
        ResultReaction reaction = suggestion.getReactionManager().getTopReaction();
        Member member = message.getGuild().retrieveMember(message.getAuthor()).complete();

        builder.setAuthor(member.getEffectiveName(), null, message.getAuthor().getEffectiveAvatarUrl());
        Color color = Color.gray;
        if (reaction != null) {
            color = reaction.getColor();
        }
        builder.setColor(color);
        builder.setDescription(String.format("\uD83D\uDCE8 [New %s posted](" + message.getJumpUrl() + ")", suggestion.getSuggestionsChannel().getPostName().toLowerCase()));
        builder.addField("\u200b", Util.trim(message.getContentRaw(), 256), false);
        builder.setFooter("Posted in #" + message.getChannel().getName());
        return builder.build();
    }

}
