package com.diamondfire.suggestionsbot.database;

import com.diamondfire.suggestionsbot.SuggestionsBot;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProvider {

    private static final MysqlDataSource source = new MysqlDataSource();

    static {
        String DB_URL = SuggestionsBot.config.DB_URL;
        String DB_USER = SuggestionsBot.config.DB_USER;
        String DB_PASS = SuggestionsBot.config.DB_PASS;

        source.setUrl(DB_URL);
        source.setUser(DB_USER);
        source.setPassword(DB_PASS);
    }

    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }

}
