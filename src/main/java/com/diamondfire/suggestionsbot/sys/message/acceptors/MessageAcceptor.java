package com.diamondfire.suggestionsbot.sys.message.acceptors;

import net.dv8tion.jda.api.entities.Message;

public interface MessageAcceptor {
    
    boolean accept(Message message);
}
