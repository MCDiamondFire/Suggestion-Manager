package com.diamondfire.suggestionsbot;

import com.diamondfire.suggestionsbot.suggestions.reactions.PopularHandler;

public class SuggestionsBot {

    public static void main(String[] args) throws InterruptedException {
        BotInstance.start();
        PopularHandler.calculate();
    }

}
