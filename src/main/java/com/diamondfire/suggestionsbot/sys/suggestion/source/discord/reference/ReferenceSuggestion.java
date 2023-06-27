package com.diamondfire.suggestionsbot.sys.suggestion.source.discord.reference;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.*;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.user.User;
import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.*;
import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.flag.*;
import net.dv8tion.jda.api.entities.Message;

public class ReferenceSuggestion implements SuggestionSite {
    
    private final Suggestion<DiscordSite> suggestion;
    
    private final ReferenceType type;
    
    private final long message;
    private final long channel;
    
    private DiscordFlagSet set;
    
    public ReferenceSuggestion(Suggestion<DiscordSite> suggestion, Message referenceMessage, ReferenceType type) {
        this.suggestion = suggestion;
        this.message = referenceMessage.getIdLong();
        this.channel = referenceMessage.getChannel().getIdLong();
        this.set = new DiscordFlagSet(referenceMessage);
        this.type = type;
    }
    
    public void refresh() {
        DiscordInstance.getJda()
                .getTextChannelById(channel)
                .editMessageById(message, type.format(suggestion))
                .queue();
    
        DiscordSite site = suggestion.getSite();
    
        // Remove flags that are not on the source flagset
        for (DiscordFlag flag : set) {
            if (!site.getFlagSet().hasFlag(flag)) {
                set.removeFlag(flag);
            }
        }
    
        // Add missing flags from source
        for (DiscordFlag flag : site.getFlagSet()) {
            if (!set.hasFlag(flag)) {
                set.addFlag(flag);
            }
        }
    }
    
    @Override
    public User getAuthor() {
        return suggestion.getAuthor();
    }
    
    @Override
    public String getContents() {
        return suggestion.getContents();
    }
    
    @Override
    public DiscordFlagSet getFlagSet() {
        return set;
    }
    
    @Override
    public SuggestionType getType() {
        return suggestion.getType();
    }
    
    public void setParentFlagSet(DiscordSite suggestion) {
        DistributingDiscordFlagSet flagSet = suggestion.getFlagSet();
        flagSet.addSet(set);
        
        this.set = suggestion.getFlagSet();
    }
}
