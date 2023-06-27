package com.diamondfire.suggestionsbot.bot.discord.command.argument.impl.types;

import com.diamondfire.suggestionsbot.bot.discord.command.argument.impl.parsing.exceptions.*;
import org.jetbrains.annotations.NotNull;

public class IntegerArgument extends AbstractSimpleValueArgument<Integer> {
    
    @Override
    public Integer parse(@NotNull String msg) throws ArgumentException {
        try {
            return Integer.parseInt(msg);
        } catch (NumberFormatException exception) {
            throw new MalformedArgumentException("Invalid number provided");
        }
    }
}
