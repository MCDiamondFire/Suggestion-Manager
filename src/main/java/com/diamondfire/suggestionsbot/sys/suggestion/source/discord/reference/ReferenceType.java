package com.diamondfire.suggestionsbot.sys.suggestion.source.discord.reference;

import com.diamondfire.suggestionsbot.sys.suggestion.impl.flags.Flag;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.user.User;
import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.DiscordSite;
import com.diamondfire.suggestionsbot.util.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;

public enum ReferenceType {
    DISCUSSION(830091257706446908L, false){
        @Override
        public MessageEmbed format(Suggestion<DiscordSite> suggestion) {
            User user = suggestion.getAuthor();
            Flag topReaction = suggestion.getFlagSet().getTopMostFlag();
            Message message = suggestion.getSite().getMessage();
            
            return new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getAvatarUrl())
                    .setColor(topReaction != null ? topReaction.getColor() : Color.gray)
                    .setDescription(String.format("\uD83D\uDCE8 [New %s posted](" + message.getJumpUrl() + ")", suggestion.getType().name()))
                    .addField("\u200b", StringUtil.trim(message.getContentRaw(), 256), false)
                    .setFooter("Posted in #" + message.getChannel().getName())
                    .build();
        }
    },
    POPULAR(830091274249437245L, true){
        @Override
        public MessageEmbed format(Suggestion<DiscordSite> suggestion) {
            User user = suggestion.getAuthor();
            Flag topReaction = suggestion.getFlagSet().getTopMostFlag();
            Message message = suggestion.getSite().getMessage();
    
    
//            PopularHandler.calculate();
//            Message message = suggestion.getSuggestion();
//            EmbedBuilder builder = new EmbedBuilder();
//            ReactionManager manager = suggestion.reactionManager;
//            Reaction reaction = manager.getTopReaction();
//            Member member = message.getGuild().retrieveMember(message.getAuthor()).complete();


//            if (suggestion.referenceManager != null) {
//                Reference reference = suggestion.referenceManager.getReference("discussion_message");
//                if (reference != null) {
//                    description.append(" or [Discussion](" + reference.getReference().getJumpUrl() + ")");
//                }
//            }
            
            return new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getAvatarUrl())
                    .setColor(topReaction != null ? topReaction.getColor() : Color.gray)
                    .setDescription(String.format("\uD83D\uDCE8 Jump to [%s](" + message.getJumpUrl() + ")", suggestion.getType().name()))
                    .addField("\u200b", StringUtil.trim(message.getContentRaw(), 256), false)
                    //.setFooter("+" + manager.getNetVotes() + " (" + manager.getUpVotes() + "|" + manager.getDownVotes() + ")")
                    .build();
        }
    };
    
    private final long channelId;
    private final boolean mustExist;
    
    ReferenceType(long channelId, boolean mustExist) {
        this.channelId = channelId;
        this.mustExist = mustExist;
    }
    
    public long getChannelId() {
        return channelId;
    }
    
    public abstract MessageEmbed format(Suggestion<DiscordSite> suggestion);
}
