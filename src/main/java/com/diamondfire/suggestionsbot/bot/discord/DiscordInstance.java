package com.diamondfire.suggestionsbot.bot.discord;

import com.diamondfire.suggestionsbot.bot.discord.command.CommandHandler;
import com.diamondfire.suggestionsbot.bot.discord.command.impl.other.*;
import com.diamondfire.suggestionsbot.bot.discord.config.Config;
import com.diamondfire.suggestionsbot.bot.discord.events.*;
import com.diamondfire.suggestionsbot.sys.tasks.TaskRegistry;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class DiscordInstance {
    
    private static final Config config = new Config();
    public static final long DF_GUILD = config.getGuild();
    public static final long LOG_CHANNEL = config.getLogChannel();
    private static final CommandHandler handler = new CommandHandler();
    
    private static JDA jda;
    private static final TaskRegistry loop = new TaskRegistry();
    
    public static void initialize() throws LoginException {
        
        handler.register(
                // others
                new MimicCommand(),
                new EvalCommand(),
                new HelpCommand(),
                new RestartCommand(),
                new DisableCommand(),
                new EnableCommand(),
                new DisableCommand(),
                new QueryCommand()
        );
        
        JDABuilder builder = JDABuilder.createDefault(config.getToken())
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setStatus(OnlineStatus.ONLINE)
                .setMemberCachePolicy(MemberCachePolicy.DEFAULT)
                .setActivity(Activity.watching("for " + getConfig().getPrefix() + "help"))
                .setGatewayEncoding(GatewayEncoding.ETF)
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.CLIENT_STATUS)
                .addEventListeners(new MessageEvent(), new ReactionEvent(), new ReadyEvent(), new GuildJoinEvent());
        
        jda = builder.build();
        handler.initialize();
    }
    
    public static JDA getJda() {
        return jda;
    }
    
    public static CommandHandler getHandler() {
        return handler;
    }
    
    public static Config getConfig() {
        return config;
    }
    
    public static TaskRegistry getScheduler() {
        return loop;
    }
}
