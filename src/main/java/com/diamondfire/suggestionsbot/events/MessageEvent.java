package com.diamondfire.suggestionsbot.events;

import com.diamondfire.suggestionsbot.util.WebUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.channels.ChannelHandler;
import com.diamondfire.suggestionsbot.util.BotConstants;
import com.diamondfire.suggestionsbot.util.SensitiveData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;

public class MessageEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        long channelID = event.getChannel().getIdLong();

        if (message.getContentDisplay().startsWith(BotConstants.PREFIX)) {
            CommandEvent commandEvent = new CommandEvent(event.getJDA(), event.getResponseNumber(), message);
            BotInstance.getHandler().run(commandEvent);
            return;
        }

        if (ChannelHandler.isValidChannel(channelID)) {
            ChannelHandler.getChannel(channelID).onMessage(message);
        }


    }

}
