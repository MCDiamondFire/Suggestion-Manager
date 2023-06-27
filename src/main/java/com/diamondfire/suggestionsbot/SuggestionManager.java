package com.diamondfire.suggestionsbot;


import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;

import javax.security.auth.login.LoginException;

public class SuggestionManager {
    
    public static void main(String[] args) throws LoginException {
        DiscordInstance.initialize();
    }
    
}
