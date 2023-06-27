package com.diamondfire.suggestionsbot.bot.discord.command.argument.impl.types;

import com.diamondfire.suggestionsbot.bot.discord.command.argument.impl.parsing.exceptions.ArgumentException;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;

public abstract class AbstractSimpleValueArgument<T> implements Argument<T> {
    
    @Override
    public T parseValue(@NotNull Deque<String> args) throws ArgumentException {
        return parse(args.pop());
    }
    
    protected abstract T parse(@NotNull String argument) throws ArgumentException;
    
}
