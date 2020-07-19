package com.diamondfire.suggestionsbot.command.argument.impl.types;

import org.jetbrains.annotations.NotNull;

public class LongArgument extends Argument<Long> {

    @Override
    public Long getValue(@NotNull String msg) throws IllegalArgumentException {
        try {
            return Long.parseLong(msg);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid long provided!");
        }
    }
}
