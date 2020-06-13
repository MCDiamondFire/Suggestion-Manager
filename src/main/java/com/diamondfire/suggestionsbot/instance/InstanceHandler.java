package com.diamondfire.suggestionsbot.instance;


import com.diamondfire.suggestionsbot.suggestions.reactions.PopularHandler;

import javax.security.auth.login.LoginException;

public class InstanceHandler {

    public void startup() throws LoginException, InterruptedException {
        BotInstance.start();
        PopularHandler.calculate();

    }
}
