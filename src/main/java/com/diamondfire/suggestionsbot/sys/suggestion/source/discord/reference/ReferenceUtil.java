package com.diamondfire.suggestionsbot.sys.suggestion.source.discord.reference;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.sys.database.impl.DatabaseQuery;
import com.diamondfire.suggestionsbot.sys.database.impl.queries.BasicQuery;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.*;

public class ReferenceUtil {
    
    public static void removeReference(long messageId) {
        new DatabaseQuery().query(new BasicQuery("DELETE FROM owen.discord_references WHERE message_id = ?", (statement) -> {
            statement.setLong(1, messageId);
        })).compile();
    }
    
    public static void createReference(ReferenceType type, Suggestion<DiscordSite> suggestion) {
        DiscordInstance.getJda().getTextChannelById(type.getChannelId()).sendMessage(type.format(suggestion)).queue((message) -> {
            new DatabaseQuery().query(new BasicQuery("INSERT INTO owen.discord_references (message_id, channel_id, suggestion_id, reference_type) VALUES (?,?,?,?)", (statement) -> {
                statement.setLong(1, message.getIdLong());
                statement.setLong(2, message.getChannel().getIdLong());
                statement.setLong(3, suggestion.getId());
                statement.setByte(4, (byte) type.ordinal());
            })).compile();
            DiscordSuggestionStorage.INSTANCE.addReferenceForwardCache(message.getIdLong(), suggestion);
            suggestion.getSite().addReference(new ReferenceSuggestion(suggestion, message, type));
        });
    }
}
