package com.diamondfire.suggestionsbot;

import com.diamondfire.suggestionsbot.suggestions.reactions.PopularHandler;
import com.diamondfire.suggestionsbot.util.config.ConfigLoader;

import javax.security.auth.login.LoginException;

public class SuggestionsBot {

    public static void main(String[] args) throws LoginException, InterruptedException {
        BotInstance.start();
        PopularHandler.calculate();
    }

}
