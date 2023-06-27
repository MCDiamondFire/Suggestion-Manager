package com.diamondfire.suggestionsbot.util;

import com.diamondfire.suggestionsbot.sys.database.impl.DatabaseQuery;
import com.diamondfire.suggestionsbot.sys.database.impl.queries.BasicQuery;
import com.diamondfire.suggestionsbot.sys.database.impl.result.DatabaseResult;

public class IdHandler {
    
    public static synchronized long getNewId() {
        try (DatabaseResult result = new DatabaseQuery()
                .query(new BasicQuery("SELECT COUNT(*) as count FROM owen.sugs"))
                .compile()
                .get()) {
            
            long count = result.getResult().getLong("count");
    
            new DatabaseQuery()
                    .query(new BasicQuery("INSERT INTO owen.sugs(id, discord, discord_channel, github) VALUES (?, null, null, null)", (statement) -> {
                        statement.setLong(1, count);
                    }))
                    .compile();
            
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        throw new IllegalStateException();
    }
}
