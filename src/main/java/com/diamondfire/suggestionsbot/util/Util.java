package com.diamondfire.suggestionsbot.util;

public class Util {

    public static String trim(String toTrim, int trimmy) {
        return toTrim.length() > trimmy ? toTrim.substring(0, trimmy) + "..." : toTrim;
    }

    public static String repeat(String ogString, String repeat, int i) {
        StringBuilder ogStringBuilder = new StringBuilder(ogString);
        for (int j = 0; j < i; j++) {
            ogStringBuilder.append(repeat);
        }
        ogString = ogStringBuilder.toString();
        return ogString;
    }

}
