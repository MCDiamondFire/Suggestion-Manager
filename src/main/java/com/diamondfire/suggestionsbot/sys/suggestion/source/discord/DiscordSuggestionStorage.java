package com.diamondfire.suggestionsbot.sys.suggestion.source.discord;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.sys.database.impl.DatabaseQuery;
import com.diamondfire.suggestionsbot.sys.database.impl.queries.BasicQuery;
import com.diamondfire.suggestionsbot.sys.database.impl.result.DatabaseResult;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.sys.suggestion.source.SuggestionSources;
import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.reference.*;
import com.diamondfire.suggestionsbot.util.WeakStorage;
import net.dv8tion.jda.api.entities.*;

import java.sql.ResultSet;

public class DiscordSuggestionStorage {
    
    public static final DiscordSuggestionStorage INSTANCE = new DiscordSuggestionStorage();
    
    private final WeakStorage<Long, Suggestion<DiscordSite>> discordSuggestionStorage = new WeakStorage<>(1800000); // 15 minutes
    private final WeakStorage<Long, Suggestion<DiscordSite>> sugIdStorage = new WeakStorage<>(1800000); // 15 minutes
    
    public void addCache(Suggestion<DiscordSite> suggestion) {
        sugIdStorage.put(suggestion.getId(), suggestion);
    }
    
    public void addReferenceForwardCache(long idLong, Suggestion<DiscordSite> suggestion) {
        discordSuggestionStorage.put(idLong, suggestion);
    }
    
    public Suggestion<DiscordSite> getSuggestion(long messageId) {
        Suggestion<DiscordSite> suggestion = discordSuggestionStorage.get(messageId);
        if (suggestion != null) {
            return suggestion;
        }
        
        // Get from main suggestion
        try (DatabaseResult result = new DatabaseQuery()
                .query(new BasicQuery("SELECT * FROM owen.sugs WHERE discord = ?", (statement) -> {
                    statement.setLong(1, messageId);
                }))
                .compile()
                .get()) {
            for (ResultSet set : result) {
                return getSuggestion(set.getLong("id"), set.getLong("discord_channel"), set.getLong("discord"), messageId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Get from references
        try (DatabaseResult result = new DatabaseQuery()
                .query(new BasicQuery("SELECT * FROM owen.discord_references, owen.sugs WHERE sugs.id = discord_references.suggestion_id AND message_id = ?",
                        (statement) -> {
                            statement.setLong(1, messageId);
                        }))
                .compile()
                .get()) {
            
            for (ResultSet entrySet : result) {
                return getSuggestion(entrySet.getLong("id"), entrySet.getLong("discord_channel"), entrySet.getLong("discord"), messageId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void removeFromCache(long messageId) {
        sugIdStorage.remove(messageId);
    }
    
    private Suggestion<DiscordSite> getSuggestion(long id, long channel, long message, long fromId) {
        if (sugIdStorage.isStored(id)) {
            Suggestion<DiscordSite> suggestion = sugIdStorage.get(id);
            DiscordSuggestionStorage.INSTANCE.addReferenceForwardCache(fromId, suggestion);
            
            return suggestion;
        }
        
        Message discordMessage = DiscordInstance.getJda().getTextChannelById(channel).retrieveMessageById(message).complete();
        Member member = discordMessage.getGuild().retrieveMember(discordMessage.getAuthor()).complete();
        DiscordSite discordSite = new DiscordSite(discordMessage, member, DiscordSuggestionChannel.getChannel(discordMessage.getChannel().getIdLong()));
        Suggestion<DiscordSite> source = new Suggestion<>(id, discordSite, SuggestionSources.DISCORD);
        
        DiscordSuggestionStorage.INSTANCE.addCache(source);
        
        new DatabaseQuery()
                .query(new BasicQuery("SELECT * FROM owen.discord_references where suggestion_id = ?",
                        (statement) -> {
                            statement.setLong(1, id);
                        }))
                .compile()
                .run((result) -> {
                    for (ResultSet set : result) {
                        Message referencedMessage = DiscordInstance.getJda()
                                .getTextChannelById(set.getLong("channel_id"))
                                .retrieveMessageById(set.getLong("message_id"))
                                .complete();
                        
                        discordSite.addReference(new ReferenceSuggestion(source, referencedMessage, ReferenceType.values()[set.getByte("reference_type")]));
                        DiscordSuggestionStorage.INSTANCE.addReferenceForwardCache(referencedMessage.getIdLong(), source);
                    }
                });
        
        return source;
    }
}
