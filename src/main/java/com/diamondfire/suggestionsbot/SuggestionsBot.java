package com.diamondfire.suggestionsbot;

import com.diamondfire.suggestionsbot.instance.InstanceHandler;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class SuggestionsBot {
    public static InstanceHandler instance = new InstanceHandler();

    public static void main(String[] args) throws LoginException, InterruptedException {
        instance.startup();
    }

}
