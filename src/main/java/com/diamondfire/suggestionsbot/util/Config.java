package com.diamondfire.suggestionsbot.util;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {

    public static final String URL_KEY = "url";
    private static final String TOKEN_KEY = "token";
    private static final String USER_KEY = "user";
    private static final String PASSWORD_KEY = "pass";
    private static final String SECRET_SUGGESTIONS_KEY = "secret-issues-webhook-url";

    public static String TOKEN;
    public static String PASSWORD;
    public static String USER;
    public static String URL;
    public static String WEBHOOK_URL;

    public static void compileConfig() throws IllegalStateException {
        Path path = Paths.get("config.txt");
        if (!Files.exists(path)) {
            try (Writer writer = Files.newBufferedWriter(path)) {
                getDefault().store(writer, "Configuration file for SugBot");
            } catch (IOException exception) {
                throw new IllegalStateException("Could not write config! " + exception.getMessage());
            }

            throw new IllegalStateException("Created default config. Please modify values!");
        } else {
            Properties properties = new Properties();
            try (Reader reader = Files.newBufferedReader(path)) {
                properties.load(reader);
            } catch (IOException exception) {
                throw new IllegalStateException("Failed to read config! " + exception.getMessage());
            }

            if (properties.equals(getDefault())) {
                throw new IllegalStateException("Please configure your config file! Default values detected.");
            }

            try {
                TOKEN = properties.getProperty(TOKEN_KEY);
                USER = properties.getProperty(USER_KEY);
                PASSWORD = properties.getProperty(PASSWORD_KEY);
                URL = properties.getProperty(URL_KEY);
                WEBHOOK_URL = properties.getProperty(SECRET_SUGGESTIONS_KEY);
            } catch (Exception exception) {
                throw new IllegalStateException("Invalid config property: " + exception.getMessage(), exception);
            }
        }

    }

    public static Properties getDefault() {
        Properties properties = new Properties();
        properties.put(TOKEN_KEY, "bot-token");
        properties.put(USER_KEY, "db-user");
        properties.put(PASSWORD_KEY, "db-password");
        properties.put(URL_KEY, "url");
        properties.put(SECRET_SUGGESTIONS_KEY, "webhook-url.com");

        return properties;
    }

}
