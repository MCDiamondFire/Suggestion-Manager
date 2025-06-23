package com.diamondfire.suggestionsbot;


import com.diamondfire.suggestionsbot.command.CommandHandler;
import com.diamondfire.suggestionsbot.command.impl.CloneCommand;
import com.diamondfire.suggestionsbot.command.impl.EvalCommand;
import com.diamondfire.suggestionsbot.command.impl.HelpCommand;
import com.diamondfire.suggestionsbot.command.impl.InfoCommand;
import com.diamondfire.suggestionsbot.command.impl.PopularCommand;
import com.diamondfire.suggestionsbot.command.impl.StatsCommand;
import com.diamondfire.suggestionsbot.command.impl.WhenCommand;
import com.diamondfire.suggestionsbot.events.MessageEvent;
import com.diamondfire.suggestionsbot.events.ReactionEvent;
import com.diamondfire.suggestionsbot.util.config.ConfigLoader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.List;

public final class BotInstance {

    private static final CommandHandler handler = new CommandHandler();
    private static JDA jda;

    private BotInstance() {
    }

    public static void start() throws InterruptedException {
        handler.register(
                new HelpCommand(),
                new StatsCommand(),
                new InfoCommand(),
                new PopularCommand(),
                new EvalCommand(),
                new CloneCommand(),
                new WhenCommand()
        );

        JDABuilder builder = JDABuilder.createDefault(ConfigLoader.getConfig().getToken());
        builder.enableIntents(List.of(GatewayIntent.MESSAGE_CONTENT));
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
