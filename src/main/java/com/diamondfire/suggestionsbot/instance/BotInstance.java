package com.diamondfire.suggestionsbot.instance;


import com.diamondfire.suggestionsbot.command.CommandHandler;
import com.diamondfire.suggestionsbot.command.impl.*;
import com.diamondfire.suggestionsbot.events.MessageEvent;
import com.diamondfire.suggestionsbot.events.PrivateMessageEvent;
import com.diamondfire.suggestionsbot.events.ReactionEvent;
import com.diamondfire.suggestionsbot.util.BotConstants;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.utils.ChunkingFilter;

import javax.security.auth.login.LoginException;

public class BotInstance {
    private static final CommandHandler handler = new CommandHandler();
    private static JDA jda;

    public static void start() throws InterruptedException, LoginException {
        handler.register(
                new HelpCommand(),
                new StatsCommand(),
                new RefreshCommand(),
                new InfoCommand(),
                new PopularCommand(),
                new EvalCommand()
        );

        JDABuilder builder = JDABuilder.createDefault(BotConstants.TOKEN);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.addEventListeners(new MessageEvent(), new ReactionEvent(), new PrivateMessageEvent());

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
