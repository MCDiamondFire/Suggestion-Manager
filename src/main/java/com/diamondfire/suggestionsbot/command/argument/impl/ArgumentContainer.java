package com.diamondfire.suggestionsbot.command.argument.impl;

import com.diamondfire.suggestionsbot.command.argument.impl.types.Argument;
import org.jetbrains.annotations.NotNull;

// This is just a wrapper class, so I don't have to deal with <> and raw usage warnings everywhere.
public record ArgumentContainer(Argument<?> argument) {

    public ArgumentContainer(@NotNull Argument<?> argument) {
        this.argument = argument;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String msg) {
        return (T) argument().getValue(msg);
    }

}
