package com.diamondfire.suggestionsbot.command.arguments.value;

public class LongArg extends ValueArgument<Long> {

    public LongArg(String name, boolean isRequired) {
        super(name, isRequired);
    }

    public LongArg(String name, Long fallbackValue) {
        super(name, fallbackValue);
    }

    @Override
    public Long getValue(String msg) {
        return Long.parseLong(msg);
    }

    @Override
    protected boolean validateValue(String msg) {
        try {
            Long.parseLong(msg);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    @Override
    public String failMessage() {
        return "Argument must contain a valid long!";
    }

    @Override
    public String toString() {
        return "Number";
    }
}
