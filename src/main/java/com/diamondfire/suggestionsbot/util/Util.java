package com.diamondfire.suggestionsbot.util;

public final class Util {

    private Util() {
    }

    public static String trim(String toTrim, int maxLength) {
        if (toTrim.length() > maxLength) {
            return toTrim.substring(0, maxLength) + "...";
        }
        return toTrim;
    }

}
