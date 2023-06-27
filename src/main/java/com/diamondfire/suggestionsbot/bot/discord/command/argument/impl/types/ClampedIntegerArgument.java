package com.diamondfire.suggestionsbot.bot.discord.command.argument.impl.types;

import com.diamondfire.suggestionsbot.bot.discord.command.argument.impl.parsing.exceptions.ArgumentException;
import com.diamondfire.suggestionsbot.util.Util;
import org.jetbrains.annotations.NotNull;

public class ClampedIntegerArgument extends IntegerArgument {
    
    final int min;
    final int max;
    
    public ClampedIntegerArgument(int min) {
        this.min = min;
        this.max = Integer.MAX_VALUE;
    }
    
    public ClampedIntegerArgument(int min, int max) {
        this.min = min;
        this.max = max;
    }
    
    @Override
    public Integer parse(@NotNull String msg) throws ArgumentException {
        int num = super.parse(msg);
        
        if (num <= max && num >= min) {
            return num;
        } else {
            return Util.clamp(num, min, max);
        }
    }
}
