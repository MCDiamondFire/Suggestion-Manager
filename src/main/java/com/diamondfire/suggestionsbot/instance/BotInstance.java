package com.diamondfire.suggestionsbot.instance;


import com.diamondfire.suggestionsbot.SuggestionsBot;
import com.diamondfire.suggestionsbot.command.CommandHandler;
import com.diamondfire.suggestionsbot.command.impl.*;
import com.diamondfire.suggestionsbot.events.MessageEvent;
import com.diamondfire.suggestionsbot.events.ReactionEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

import javax.security.auth.login.LoginException;

public class BotInstance {
    private static final CommandHandler handler = new CommandHandler();
    private static JDA jda;

    public static void start() throws InterruptedException, LoginException {
        String token = SuggestionsBot.config.TOKEN;
        if (token == null) {
            token = System.getenv("DISCORD_TOKEN");
        }

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.addEventListeners(new MessageEvent(), new ReactionEvent(), new CommandHandler());

        jda = builder.build();
        jda.awaitReady();

        handler.register(
                new HelpCommand(),
                new StatsCommand(),
                new InfoCommand(),
                new PopularCommand(),
                new EvalCommand(),
                new WhenCommand()
        );
    }

    public static JDA getJda() {
        return jda;
    }

    public static CommandHandler getHandler() {
        return handler;
    }


}
