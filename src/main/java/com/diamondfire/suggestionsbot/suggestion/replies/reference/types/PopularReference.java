package com.diamondfire.suggestionsbot.suggestion.replies.reference.types;

import com.diamondfire.suggestionsbot.guild.BotGuilds;
import com.diamondfire.suggestionsbot.reactions.PopularHandler;
import com.diamondfire.suggestionsbot.reactions.ResultReaction;
import com.diamondfire.suggestionsbot.suggestion.ReactionManager;
import com.diamondfire.suggestionsbot.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.suggestion.replies.reference.Reference;
import com.diamondfire.suggestionsbot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.Color;

public class PopularReference extends Reference {

    private final TextChannel channel;

    public PopularReference(long guildId) {
        this.channel = BotGuilds.get(guildId).getPopularChannel();
    }

    @Override
    public String getName() {
        return "popular_message";
    }

    @Override
    public TextChannel getChannel() {
        return this.channel;
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
        ReactionManager manager = suggestion.getReactionManager();
        ResultReaction reaction = manager.getTopReaction();
        Member member = message.getGuild().retrieveMember(message.getAuthor()).complete();

        StringBuilder description = new StringBuilder();
        description.append(String.format("\uD83D\uDCE8 Jump to [%s](" + message.getJumpUrl() + ")", suggestion.getSuggestionsChannel().getPostName()));

        //TODO Fix issue where if the popular message isn't there, it doesn't correctly fetches the discussion message.
        // The reason for this, is quite simple.
        Reference reference = suggestion.getReferenceManager().getReference("discussion_message");
        if (reference != null) {
            description.append(" or [Discussion](").append(reference.getReference().getJumpUrl()).append(")");
        }

        builder.setDescription(description.toString());
        builder.addField("\u200b", Util.trim(message.getContentRaw(), 256), false);
        builder.setAuthor(member.getEffectiveName(), null, message.getAuthor().getEffectiveAvatarUrl());
        Color color = Color.gray;
        if (reaction != null) {
            color = reaction.getColor();
        }
        builder.setColor(color);
        builder.setFooter("+" + manager.getNetVotes() + " (" + manager.getUpVotes() + "|" + manager.getDownVotes() + ")");
        return builder.build();
    }

}
