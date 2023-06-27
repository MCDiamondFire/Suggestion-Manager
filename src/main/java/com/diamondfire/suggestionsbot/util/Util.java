package com.diamondfire.suggestionsbot.util;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.bot.discord.command.reply.PresetBuilder;
import com.google.gson.*;
import net.dv8tion.jda.api.EmbedBuilder;

import java.io.*;
import java.net.*;
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
    
//    public static String getPlayerHead(String player) {
//        return "https://external-content.duckduckgo.com/iu/?reload=" + System.currentTimeMillis() + "&u=" + "https://mc-heads.net/head/" + URLEncoder.encode(player, StandardCharsets.UTF_8) + "/180";
//    }
    
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
    
    public static void log(PresetBuilder builder) {
        log(builder.getEmbed());
    }
    
    public static void log(EmbedBuilder builder) {
        DiscordInstance.getJda().getTextChannelById(DiscordInstance.LOG_CHANNEL).sendMessage(builder.build()).queue();
    }
    
}
