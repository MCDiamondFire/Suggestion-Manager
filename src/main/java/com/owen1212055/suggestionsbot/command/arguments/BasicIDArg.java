package com.owen1212055.suggestionsbot.command.arguments;

public class BasicIDArg extends Argument {
    @Override
    public boolean validate(String args) {
        try {
            Long.parseLong(args);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String failMessage() {
        return "Invalid ID! Make sure it is formatted correctly...";
    }

    @Override
    public String toString() {
        return "<ID>";
    }
}
