package com.diamondfire.suggestionsbot.util;

public class Util {

    public static String trim(String toTrim, int trimmy) {
        return toTrim.length() > trimmy ? toTrim.substring(0, trimmy) + "..." : toTrim;
    }


}
