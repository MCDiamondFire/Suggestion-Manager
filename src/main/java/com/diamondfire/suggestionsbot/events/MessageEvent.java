package com.diamondfire.suggestionsbot.events;

import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.channels.ChannelHandler;
import com.diamondfire.suggestionsbot.util.BotConstants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        long channelID = event.getChannel().getIdLong();

        if (message.getContentDisplay().startsWith(BotConstants.PREFIX)) {
            try {
                CommandEvent commandEvent = new CommandEvent(event.getJDA(), event.getResponseNumber(), event.getMessage());
                BotInstance.getHandler().run(commandEvent);
                return;
            } catch (IllegalArgumentException ignored) {
            }
        }

        if (ChannelHandler.isValidChannel(channelID)) {
            if (!event.getMember().getRoles().contains(event.getGuild().getRoleById(530860699937669130L))) {
                ChannelHandler.getChannel(channelID).onMessage(message);
            } else {
                event.getAuthor().openPrivateChannel().queue((privateChannel -> {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("Notice!");
                    builder.setDescription("You are unable to suggest at this time. If you are curious as to why that is the case, please contact a moderator. I am sending you your suggestion right now so you can resend it later.");
                    privateChannel.sendMessageEmbeds(builder.build()).queue();
                    event.getMessage().forwardTo(privateChannel).queue();

                }));
                event.getMessage().delete().reason("User has no-suggest role!").queue();
            }
        }


    }

}
