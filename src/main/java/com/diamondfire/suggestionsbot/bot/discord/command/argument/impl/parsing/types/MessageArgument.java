package com.diamondfire.suggestionsbot.bot.discord.command.argument.impl.parsing.types;

import com.diamondfire.suggestionsbot.bot.discord.command.argument.impl.parsing.exceptions.ArgumentException;
import com.diamondfire.suggestionsbot.bot.discord.command.argument.impl.types.Argument;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;

public class MessageArgument implements Argument<String> {
    
    @Override
    public String parseValue(@NotNull Deque<String> args) throws ArgumentException {
        return String.join(" ", args);
    }
}
