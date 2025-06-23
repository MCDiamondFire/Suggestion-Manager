package com.diamondfire.suggestionsbot.database;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SimpleSingleQuery {

    private String query;
    private PreparedStatementManager preparedStatement;

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
             PreparedStatement statement = connection.prepareStatement(this.query)) {
            if (this.preparedStatement != null) {
                this.preparedStatement.run(statement);
            }

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
