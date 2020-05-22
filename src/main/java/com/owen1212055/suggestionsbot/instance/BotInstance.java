package com.owen1212055.suggestionsbot.instance;


import com.owen1212055.suggestionsbot.command.CommandHandler;
import com.owen1212055.suggestionsbot.command.commands.*;
import com.owen1212055.suggestionsbot.events.MessageEvent;
import com.owen1212055.suggestionsbot.events.ReactionEvent;
import com.owen1212055.suggestionsbot.util.BotConstants;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

import javax.security.auth.login.LoginException;

public class BotInstance {
    private static final CommandHandler handler = new CommandHandler();
    private static JDA jda;

    public static void start() throws InterruptedException, LoginException {
        handler.register(
                // query commands
                new HelpCommand(),
                new DeleteCommand(),
                new StatsCommand(),
                new RefreshCommand(),
                new InfoCommand(),
                new PopularCommand()
        );

        JDABuilder builder = JDABuilder.createDefault(BotConstants.TOKEN);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.addEventListeners(new MessageEvent(), new ReactionEvent());

        jda = builder.build();
        jda.awaitReady();
    }

    public static JDA getJda() {
        return jda;
    }

    public static CommandHandler getHandler() {
        return handler;
    }


}
