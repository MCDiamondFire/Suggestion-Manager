package com.diamondfire.suggestionsbot.util.config;

import com.diamondfire.suggestionsbot.util.config.deserializer.ConfigReactionDeserializer;
import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigReaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public final class ConfigLoader {

    private static final String CONFIG_FILE_NAME = "config.json";
    private static Config config;

    private ConfigLoader() {
    }

    private static void load() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ConfigReaction.class, new ConfigReactionDeserializer())
                .create();
        try (Reader reader = new FileReader(CONFIG_FILE_NAME)) {
            config = gson.fromJson(reader, Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Config getConfig() {
        if (config == null) {
            load();
        }
        return config;
    }

}
