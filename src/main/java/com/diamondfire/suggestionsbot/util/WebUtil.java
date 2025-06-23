package com.diamondfire.suggestionsbot.util;

import com.google.gson.JsonObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtil {

    public static void webHook(String url, JsonObject object) {
        HttpURLConnection connection = null;
        try {

            connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setDoOutput(true);


            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            outputStream.writeBytes(object.toString());

            outputStream.close();

            connection.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void webHook(String url, String string) {
        HttpURLConnection connection = null;
        try {

            connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setDoOutput(true);


            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            JsonObject object = new JsonObject();
            object.addProperty("content", string);
            outputStream.writeBytes(object.toString());

            outputStream.close();

            connection.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
