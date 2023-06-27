package com.diamondfire.suggestionsbot.bot.discord.events;

import com.diamondfire.suggestionsbot.sys.message.acceptors.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class MessageEvent extends ListenerAdapter {
    
    private static final MessageAcceptor[] acceptors = new MessageAcceptor[]{new SuggestionAcceptor(), new CommandAcceptor()};
    
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        for (MessageAcceptor acceptor : acceptors) {
            if (acceptor.accept(message)) {
                break;
            }
        }
        
    }
    
}
