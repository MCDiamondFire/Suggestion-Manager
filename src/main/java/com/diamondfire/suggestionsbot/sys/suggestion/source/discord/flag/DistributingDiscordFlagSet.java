package com.diamondfire.suggestionsbot.sys.suggestion.source.discord.flag;

import com.diamondfire.suggestionsbot.sys.suggestion.impl.flags.FlagSet;
import net.dv8tion.jda.api.entities.Message;

import java.util.*;

public class DistributingDiscordFlagSet extends DiscordFlagSet {
    
    private final List<FlagSet<DiscordFlag>> subscribers = new ArrayList<>();
    
    public DistributingDiscordFlagSet(Message message) {
        super(message);
    }
    
    public void addSet(FlagSet<DiscordFlag> flags) {
        subscribers.add(flags);
    }
    
    @Override
    public void addFlag(DiscordFlag flag) {
        super.addFlag(flag);
        for (FlagSet<DiscordFlag> flags : subscribers) {
            flags.addFlag(flag);
        }
    }
    
    @Override
    public void removeFlag(DiscordFlag flag) {
        super.removeFlag(flag);
        for (FlagSet<DiscordFlag> flags : subscribers) {
            flags.removeFlag(flag);
        }
    }
}
