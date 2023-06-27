package com.diamondfire.suggestionsbot.sys.database.impl.result;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.sql.*;
import java.util.Iterator;

public class DatabaseResult implements Iterable<ResultSet>, Closeable {
    
    private final ResultSet set;
    private final Connection connection;
    
    public DatabaseResult(ResultSet set, Connection connection) {
        this.set = set;
        this.connection = connection;
        if (set == null) {
            close();
        }
    }
    
    public boolean isEmpty() {
        if (set == null) {
            return true;
        }
        
        try {
            if (!set.next()) {
                return true;
            } else {
                set.beforeFirst();
            }
        } catch (SQLException ignored) {
        }
        
        return false;
    }
    
    public ResultSet getResult() {
        try {
            set.next();
        } catch (SQLException ignored) {
        }
        
        return set;
    }
    
    @NotNull
    @Override
    public Iterator<ResultSet> iterator() {
        ResultSet set = getResult();
        try {
            set.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Iterator() {
            @Override
            public boolean hasNext() {
                try {
                    return set.next();
                } catch (SQLException ignored) {
                    return false;
                }
            }
            
            @Override
            public ResultSet next() {
                return set;
            }
        };
    }
    
    @Override
    public void close() {
        try {
            if (set != null) {
                set.close();
            }
            connection.close();
        } catch (Exception e) {
        }
    }
}
