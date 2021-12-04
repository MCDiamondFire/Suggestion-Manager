package com.diamondfire.suggestionsbot.util;

import net.dv8tion.jda.api.utils.MarkdownSanitizer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class StringUtil {

    public static String listView(String[] array, String pointer, boolean sanitize) {
        if (array.length == 0) {
            return "";
        }

        String list = ("\n%s% " + String.join("\n%s% ", array)).replaceAll("%s%", pointer);


        return sanitize ? MarkdownSanitizer.escape(list) : list;
    }

    public static String asciidocStyle(HashMap<String, Integer> hashes) {
        if (hashes.size() == 0) {
            return "";
        }

        String longest = hashes.keySet().stream().max(Comparator.comparingInt(String::length)).orElse(null);

        ArrayList<String> strings = new ArrayList<>();

        hashes.entrySet()
                .forEach((stringIntegerEntry -> strings.add(stringIntegerEntry.getKey() +
                        Util.repeat("", " ", (longest.length() + 2) - stringIntegerEntry.getKey().length()) + ":: " + stringIntegerEntry.getValue())));

        return String.join("\n", strings);
    }

    public static String fieldSafe(String string) {
        if (string.length() >= 950) {
            return string.substring(0, 950) + "...";
        }
        return string;
    }

    public static String fieldSafe(Object object) {
        return fieldSafe(String.valueOf(object));
    }

    public static String titleSafe(String string) {
        if (string.length() >= 200) {
            return string.substring(0, 200);
        }
        return string;
    }

    public static String smartCaps(String text) {
        String[] words = text.split(" ");
        StringBuilder builder = new StringBuilder();

        for (String word : words) {
            if (word.length() < 2) {
                builder.append(word.toLowerCase() + " ");
                continue;
            }

            builder.append(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ");
        }
        return builder.toString();

    }

}
