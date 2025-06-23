package com.diamondfire.suggestionsbot.command.argument;

import com.diamondfire.suggestionsbot.command.argument.impl.ArgumentContainer;
import com.diamondfire.suggestionsbot.command.argument.impl.types.Argument;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class ArgumentSet {

    private final Map<String, ArgumentContainer> arguments = new LinkedHashMap<>();

    public ArgumentSet addArgument(@NotNull String name, @NotNull Argument<?> argument) {
        this.arguments.put(name, new ArgumentContainer(argument));
        return this;
    }

    public Map<String, ArgumentContainer> getArguments() {
        return this.arguments;
    }

}
