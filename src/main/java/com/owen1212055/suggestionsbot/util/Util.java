package com.owen1212055.suggestionsbot.util;

public class Util {

    public static String trim(String toTrim, int trimmy) {
        return toTrim.length() > trimmy ? toTrim.substring(0, trimmy) + "..." : toTrim;
    }


}
