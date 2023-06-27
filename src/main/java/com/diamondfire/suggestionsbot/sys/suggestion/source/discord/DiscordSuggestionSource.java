package com.diamondfire.suggestionsbot.sys.suggestion.source.discord;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.sys.database.impl.DatabaseQuery;
import com.diamondfire.suggestionsbot.sys.database.impl.queries.BasicQuery;
import com.diamondfire.suggestionsbot.sys.database.impl.result.DatabaseResult;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.flags.Flag;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.*;
import com.diamondfire.suggestionsbot.sys.suggestion.source.SuggestionSource;
import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.reference.*;
import com.diamondfire.suggestionsbot.util.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.sql.ResultSet;

public class DiscordSuggestionSource implements SuggestionSource<DiscordSite> {
    
    @Override
    public String getSourceName() {
        return "discord";
    }
    
    @Override
    public DiscordSite createSite(Suggestion<?> suggestion) {
        DiscordSuggestionChannel channel = DiscordSuggestionChannel.getChannel(suggestion.getType());
        
        Message message = DiscordInstance.getJda().getTextChannelById(channel.getChannelId()).sendMessage(formatEmbed(suggestion)).complete();
        return new DiscordSite(message, channel);
    }
    
    @Override
    public void updateSite(Suggestion<DiscordSite> suggestion) {
        for (ReferenceSuggestion referenceSuggestion : suggestion.getSite().getReferences()) {
            referenceSuggestion.refresh();
        }
    }
    
    @Override
    public void storeSuggestion(Suggestion<DiscordSite> suggestion) {
        Message message = suggestion.getSite().getMessage();
        new DatabaseQuery()
                .query(new BasicQuery("UPDATE owen.sugs SET discord = ?, discord_channel = ? WHERE id = ?", (statement) -> {
                    statement.setLong(1, message.getIdLong());
                    statement.setLong(2, message.getChannel().getIdLong());
                    statement.setLong(3, suggestion.getId());
                }))
                .compile();
    }
    
    @Override
    public DiscordSite getSuggestion(Suggestion<?> site) {
        DiscordSuggestionReference reference = getReference(site.getId());
        if (reference == null) {
            throw new IllegalStateException();
        }
        
        Message message = DiscordInstance.getJda().getTextChannelById(reference.getChannelId()).retrieveMessageById(reference.getChannelId()).complete();
        if (message == null) {
            throw new IllegalStateException();
        }
        
        return new DiscordSite(message, DiscordSuggestionChannel.getChannel(reference.getMessageId()));
    }
    
    @Override
    public boolean hasSuggestion(Suggestion<?> site) {
        return getReference(site.getId()) != null;
    }
    
    private DiscordSuggestionReference getReference(long id) {
        try (DatabaseResult result = new DatabaseQuery()
                .query(new BasicQuery("SELECT discord, discord_channel FROM owen.sugs WHERE id = ?", (statement) -> {
                    statement.setLong(1, id);
                }))
                .compile()
                .get()) {
            ResultSet set = result.getResult();
        
            if (set.next()) {
                return new DiscordSuggestionReference(set.getLong("discord"), set.getLong("discord_channel"));
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return null;
    }
    
    
    private MessageEmbed formatEmbed(SuggestionSite suggestion) {
        Flag flag = suggestion.getFlagSet().getTopMostFlag();
        
        EmbedBuilder builder = new EmbedBuilder()
                .addField("\u200b", StringUtil.trim(suggestion.getContents(), 256), false)
                .setAuthor(suggestion.getAuthor().getName(), null, suggestion.getAuthor().getAvatarUrl())
                .setColor(flag != null ? flag.getColor() : Color.gray);
        //.setFooter("+" + manager.getNetVotes() + " (" + manager.getUpVotes() + "|" + manager.getDownVotes() + ")");
        return builder.build();
    }
    
    private static class DiscordSuggestionReference {
        
        private final long messageId;
        private final long channelId;
        
        public DiscordSuggestionReference(long messageId, long channelId) {
            this.messageId = messageId;
            this.channelId = channelId;
        }
        
        public long getChannelId() {
            return channelId;
        }
        
        public long getMessageId() {
            return messageId;
        }
    }
}
