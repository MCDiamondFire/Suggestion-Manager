package com.diamondfire.suggestionsbot.util.chatfilter;

import com.diamondfire.suggestionsbot.util.MessageAcceptor;
import net.dv8tion.jda.api.entities.Message;

public class FilterAcceptor implements MessageAcceptor {
    
    @Override
    public boolean accept(Message message) {
        return !ChatFilters.filterMessage(message);
    }
}