package com.diamondfire.suggestionsbot.util;

import com.diamondfire.suggestionsbot.SuggestionsBot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.*;

public class Util {

    public static Deque<String> getUnicodeNumbers() {
        Deque<String> nums = new ArrayDeque<>();
        nums.add("\u0031\uFE0F\u20E3");
        nums.add("\u0032\uFE0F\u20E3");
        nums.add("\u0033\uFE0F\u20E3");
        nums.add("\u0034\uFE0F\u20E3");
        nums.add("\u0035\uFE0F\u20E3");
        nums.add("\u0036\uFE0F\u20E3");
        nums.add("\u0037\uFE0F\u20E3");
        nums.add("\u0038\uFE0F\u20E3");
        nums.add("\u0039\uFE0F\u20E3");
        nums.add("\uD83D\uDD1F");
        return nums;
    }

    public static UUID toUuid(String str) {
        if (str.contains("-")) {
            return UUID.fromString(str);
        } else {
            return UUID.fromString(str.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
        }
    }

    /**
     * Converts a jsonArray into a String[]
     */
    public static String[] fromJsonArray(JsonArray jsonArray) {
        if (jsonArray == null) {
            return new String[]{};
        }
        String[] string = new String[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            string[i] = jsonArray.get(i).getAsString();
        }

        return string;
    }

    public static int clamp(int num, int min, int max) {
        return Math.max(min, Math.min(num, max));
    }

    public static JsonObject getPlayerProfile(String player) {
        try {
            URL profile = new URL("https://mc-heads.net/minecraft/profile/" + player);
            URLConnection connection = profile.openConnection();
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                stringBuilder.append(inputLine);

            JsonElement element = JsonParser.parseString(stringBuilder.toString());
            if (!element.isJsonObject()) {
                return null;
            }

            return element.getAsJsonObject();
        } catch (IOException ignored) {
        }

        return null;
    }

    public static List<ActionRow> of(List<Button> components) {
        Deque<Button> buttons = new ArrayDeque<>(components);
        List<Button> queue = new ArrayList<>();

        List<ActionRow> rows = new ArrayList<>();

        while (!buttons.isEmpty()) {
            queue.add(buttons.pop());
            if (queue.size() >= 5) {
                rows.add(ActionRow.of(queue));
                queue.clear();
            }
        }

        if (!queue.isEmpty()) {
            rows.add(ActionRow.of(queue));
        }

        return rows;
    }

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
