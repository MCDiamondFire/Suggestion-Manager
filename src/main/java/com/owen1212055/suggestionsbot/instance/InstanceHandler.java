package com.owen1212055.suggestionsbot.instance;


import com.owen1212055.suggestionsbot.suggestions.reactions.PopularHandler;

import javax.security.auth.login.LoginException;

public class InstanceHandler {

    public void startup() throws LoginException, InterruptedException {

        BotInstance.start();
        PopularHandler.calculate();


    }
}
