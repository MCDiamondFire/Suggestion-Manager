package com.diamondfire.suggestionsbot.sys.suggestion.source.discord.flag;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.flags.FlagSet;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DiscordFlagSet implements FlagSet<DiscordFlag> {
    
    private final long source;
    private final long channel;
    
    private final long guild;
    
    private final EnumSet<DiscordFlag> flags = EnumSet.noneOf(DiscordFlag.class);
    
    public DiscordFlagSet(Message source) {
        this.source = source.getIdLong();
        this.channel = source.getChannel().getIdLong();
        
        this.guild = source.getGuild().getIdLong();
        
        for (MessageReaction reaction : source.getReactions()) {
            if (reaction.getReactionEmote().isEmoji()) {
                return;
            }
            
            flags.add(DiscordFlag.getFlag(reaction.getReactionEmote().getIdLong()));
        }
    }
    
    @Override
    public void addFlag(DiscordFlag flag) {
        flags.add(flag);
    
        long id = flag.getEmote().getIdLong();
        
        DiscordInstance.getJda()
                .getGuildById(guild)
                .getTextChannelById(channel)
                .retrieveMessageById(source)
                .flatMap((msg) -> msg.getReactionById(id) == null, (msg) -> msg.addReaction(flag.getEmote()))
                .queue();
    }
    
    @Override
    public void removeFlag(DiscordFlag flag) {
        flags.remove(flag);
    
        DiscordInstance.getJda()
                .getGuildById(guild)
                .getTextChannelById(channel)
                .retrieveMessageById(source)
                .flatMap((message) -> message.clearReactions(flag.getEmote()))
                .queue();
    }
    
    @Override
    public boolean hasFlag(DiscordFlag flag) {
        return flags.contains(flag);
    }
    
    @Override
    public DiscordFlag getTopMostFlag() {
        DiscordFlag topMostFlag = null;
        for (DiscordFlag flag : DiscordFlag.values()) {
            if (hasFlag(flag)) {
                if (topMostFlag == null || topMostFlag.getPriority() <= flag.getPriority()) {
                    topMostFlag = flag;
                }
            }
        }
        
        return topMostFlag;
    }
    
    @NotNull
    @Override
    public Iterator<DiscordFlag> iterator() {
        return flags.iterator();
    }
    
    public EnumSet<DiscordFlag> getFlags() {
        return flags;
    }
}
