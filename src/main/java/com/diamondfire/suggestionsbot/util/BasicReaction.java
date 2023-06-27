package com.diamondfire.suggestionsbot.util;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;

public class BasicReaction {
    
    final boolean isUnicode;
    String unicode;
    long id;
    
    public BasicReaction(String unicode) {
        isUnicode = true;
        this.unicode = unicode;
    }
    
    public BasicReaction(long emoteID) {
        isUnicode = false;
        this.id = emoteID;
    }
    
    
    public String getUnicode() {
        if (!isUnicode) {
            throw new IllegalStateException("Emoji is not a unicode char!");
        }
        
        return unicode;
    }
    
    public Emote getEmote() {
        if (isUnicode) {
            throw new IllegalStateException("Emoji is a unicode char!");
        }
        
        return DiscordInstance.getJda().getEmoteById(id);
    }
    
    public RestAction<Void> react(Message message) {
        if (isUnicode) {
            return message.addReaction(getUnicode());
        }
        
        return message.addReaction(getEmote());
    }
    
    @Override
    public String toString() {
        if (isUnicode) {
            return getUnicode();
        }
        
        return getEmote().getAsMention();
    }
    
    public boolean equalToReaction(MessageReaction.ReactionEmote reaction) {
        // if reaction is emoji yet this isn't unicode error is thrown.
        if (reaction.isEmoji() != isUnicode) {
            return false;
        }
        
        if (reaction.isEmoji()) return getUnicode().equals(reaction.getEmoji());
        if (!reaction.isEmoji()) return getEmote().getIdLong() == reaction.getIdLong();
        return false;
    }
}
