package com.diamondfire.suggestionsbot.sys.message.acceptors;

import com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.sys.suggestion.source.SuggestionSources;
import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.*;
import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.reference.*;
import net.dv8tion.jda.api.entities.Message;

public class SuggestionAcceptor implements MessageAcceptor {
    
    @Override
    public boolean accept(Message message) {
        if (message.getContentRaw().startsWith("TEST ")) {
            DiscordSite site = new DiscordSite(message, DiscordSuggestionChannel.getChannel(message.getChannel().getIdLong()));
            Suggestion<DiscordSite> suggestion = SuggestionSources.DISCORD.createSuggestion(site);
            SuggestionSources.DISCORD.storeSuggestion(suggestion);
            
            ReferenceUtil.createReference(ReferenceType.DISCUSSION, suggestion);
            DiscordSuggestionStorage.INSTANCE.addCache(suggestion);
            return true;
        }
        
        return false;
    }
}
