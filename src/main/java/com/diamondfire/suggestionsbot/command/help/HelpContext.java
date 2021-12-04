package com.diamondfire.suggestionsbot.command.help;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpContext {

    String description = null;
    CommandCategory commandCategory = null;
    List<HelpContextArgument> arguments = new ArrayList<>();

    public HelpContext description(String description) {
        this.description = description;
        return this;
    }

    public HelpContext category(CommandCategory commandCategory) {
        this.commandCategory = commandCategory;
        return this;
    }

    public HelpContext addArgument(HelpContextArgument argument) {
        arguments.add(argument);
        return this;
    }

    public HelpContext addArgument(HelpContextArgument... argument) {
        arguments.addAll(Arrays.asList(argument));
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CommandCategory getCommandCategory() {
        return commandCategory;
    }

    public List<HelpContextArgument> getArguments() {
        return arguments;
    }
}
