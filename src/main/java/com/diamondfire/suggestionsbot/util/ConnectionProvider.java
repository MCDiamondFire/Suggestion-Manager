package com.diamondfire.suggestionsbot.util;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProvider {
    private static BasicDataSource source = new BasicDataSource();

    static {
        source.setUrl(SensitiveData.DB_URL);
        source.setUsername(SensitiveData.DB_USER);
        source.setPassword(SensitiveData.DB_PASS);
        source.setMinIdle(5);
        source.setMaxIdle(10);
        source.setMaxOpenPreparedStatements(100);
    }

    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }

}