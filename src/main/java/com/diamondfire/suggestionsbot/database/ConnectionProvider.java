package com.diamondfire.suggestionsbot.database;

import com.diamondfire.suggestionsbot.util.config.ConfigLoader;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionProvider {

    private static final MysqlDataSource source = new MysqlDataSource();

    static {
        source.setUrl(ConfigLoader.getConfig().getJdbcUrl());
        source.setUser(ConfigLoader.getConfig().getDbUser());
        source.setPassword(ConfigLoader.getConfig().getDbPassword());
    }

    private ConnectionProvider() {
    }

    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }

}
