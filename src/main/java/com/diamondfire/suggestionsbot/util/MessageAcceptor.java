package com.diamondfire.suggestionsbot.util;

import net.dv8tion.jda.api.entities.Message;

public interface MessageAcceptor {
    boolean accept(Message message);
}