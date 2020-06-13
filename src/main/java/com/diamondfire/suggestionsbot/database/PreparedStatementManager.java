package com.diamondfire.suggestionsbot.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementManager {
    void run(PreparedStatement statement) throws SQLException;
}
