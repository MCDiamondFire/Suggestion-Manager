package com.diamondfire.suggestionsbot;

import com.diamondfire.suggestionsbot.instance.InstanceHandler;
import com.google.gson.Gson;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SuggestionsBot {
    public static InstanceHandler instance = new InstanceHandler();
    public static final Gson GSON = new Gson();
    public static Config config;

    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
        config = GSON.fromJson(Files.readString(Path.of("config.json")), Config.class);
        instance.startup();
    }
}
