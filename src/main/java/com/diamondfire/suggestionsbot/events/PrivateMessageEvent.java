package com.diamondfire.suggestionsbot.events;

import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.channels.ChannelHandler;
import com.diamondfire.suggestionsbot.util.BotConstants;
import com.diamondfire.suggestionsbot.util.SensitiveData;
import com.diamondfire.suggestionsbot.util.WebUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public class PrivateMessageEvent extends ListenerAdapter {

    //TODO Pretty this up lol
    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        EmbedBuilder sentMSG = new EmbedBuilder();
        sentMSG.setTitle("Thank you for your report!");
        sentMSG.setDescription("You have successfully reported your message as a **dangerous issue**. If this was a mistake, please make sure to notify us in <#528932649394241536>.");
        sentMSG.setColor(Color.GREEN);
        event.getChannel().sendMessage(sentMSG.build()).queue();

        EmbedBuilder builderIssue = new EmbedBuilder();
        builderIssue.setDescription(event.getMessage().getContentRaw());
        builderIssue.setAuthor(event.getMessage().getAuthor().getName(), null, event.getMessage().getAuthor().getEffectiveAvatarUrl());
        builderIssue.setTitle("New Dangerous Issue!");
        builderIssue.setColor(Color.RED);

        // Attachment handling
        List<Message.Attachment> attachmentList = event.getMessage().getAttachments();
        if (attachmentList.size() != 0) {
            StringBuilder attachments = new StringBuilder();
            for (Message.Attachment attachment : attachmentList) {
                attachments.append(attachment.getProxyUrl() + "\n");
            }
            builderIssue.addField("Attachments", attachments.toString(), false);
        }

        JsonObject sendObject = new JsonObject();
        JsonArray array = new JsonArray();
        array.add(JsonParser.parseString(new Gson().toJson(builderIssue.build().toData().toMap())));
        sendObject.add("embeds", array);
        sendObject.addProperty("username", "Dangerous Issue");
        sendObject.addProperty("avatar_url", "https://cdn.discordapp.com/emojis/274789151000363029.png?v=1");
        WebUtil.webHook(SensitiveData.WEB_REQUEST_URL, sendObject);

    }
}
