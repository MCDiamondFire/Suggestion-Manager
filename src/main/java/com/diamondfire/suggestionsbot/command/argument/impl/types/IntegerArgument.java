package com.diamondfire.suggestionsbot.command.argument.impl.types;

import org.jetbrains.annotations.NotNull;

public class IntegerArgument extends Argument<Integer> {

    @Override
    public Integer getValue(@NotNull String msg) throws IllegalArgumentException {
        try {
            return Integer.parseInt(msg);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid integer provided!");
        }
    }

}
