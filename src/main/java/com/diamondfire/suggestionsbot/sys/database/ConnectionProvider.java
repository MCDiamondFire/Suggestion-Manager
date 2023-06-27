package com.diamondfire.suggestionsbot.sys.database;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.bot.discord.config.Config;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;

public class ConnectionProvider {
    
    private static final MysqlDataSource source = new MysqlDataSource();
    
    static {
        Config config = DiscordInstance.getConfig();
        source.setUrl(config.getDBUrl());
        source.setUser(config.getDBUser());
        source.setPassword(config.getDBPassword());
    }
    
    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }
    
}
