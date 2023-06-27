package com.diamondfire.suggestionsbot.sys.suggestion.source.discord;

import com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.*;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.user.User;
import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.flag.*;
import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.reference.ReferenceSuggestion;
import net.dv8tion.jda.api.entities.*;

import java.util.*;

public class DiscordSite implements SuggestionSite {
    
    private final DiscordUser user;
    
    private final Message message;
    private final DistributingDiscordFlagSet flags;
    private final DiscordSuggestionChannel channel;
    
    private final List<ReferenceSuggestion> references = new ArrayList<>();
    
    public DiscordSite(Message message, DiscordSuggestionChannel channel) {
        this(message, message.getMember(), channel);
    }
    
    public DiscordSite(Message message, Member member, DiscordSuggestionChannel channel) {
        this.user = new DiscordUser(member);
        this.message = message;
        this.channel = channel;
        this.flags = new DistributingDiscordFlagSet(message);
    }
    
    @Override
    public User getAuthor() {
        return user;
    }
    
    @Override
    public String getContents() {
        return message.getContentRaw();
    }
    
    @Override
    public DistributingDiscordFlagSet getFlagSet() {
        return flags;
    }
    
    @Override
    public SuggestionType getType() {
        return channel.getType();
    }
    
    public Message getMessage() {
        return message;
    }
    
    public void addReference(ReferenceSuggestion referenceSuggestion) {
        references.add(referenceSuggestion);
        referenceSuggestion.setParentFlagSet(this);
    }
    
    public List<ReferenceSuggestion> getReferences() {
        return references;
    }
}
