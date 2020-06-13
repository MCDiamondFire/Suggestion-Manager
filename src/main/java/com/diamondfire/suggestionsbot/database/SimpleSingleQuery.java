package com.diamondfire.suggestionsbot.database;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class SimpleSingleQuery {

    String query;
    PreparedStatementManager preparedStatement;

    public SimpleSingleQuery query(@NotNull @Language("SQL") String query, @NotNull PreparedStatementManager statement) {
        this.query = query;
        this.preparedStatement = statement;
        return this;
    }

    public SimpleSingleQuery query(@NotNull @Language("SQL") String query) {
        this.query = query;
        return this;
    }
    public void execute() {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            if (preparedStatement != null) {
                preparedStatement.run(statement);
            }

           statement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
