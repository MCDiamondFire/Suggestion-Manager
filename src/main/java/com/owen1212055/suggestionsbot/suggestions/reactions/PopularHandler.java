package com.owen1212055.suggestionsbot.suggestions.reactions;

import com.owen1212055.suggestionsbot.instance.BotInstance;
import com.owen1212055.suggestionsbot.util.BotConstants;
import com.owen1212055.suggestionsbot.util.ConnectionProvider;
import net.dv8tion.jda.api.entities.Activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PopularHandler {
    public static int ratio;

    public static void calculate() {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT (SUM(upvotes) - SUM(downvotes)) / COUNT(*) as ratio FROM suggestions WHERE popular_message > 0 AND date > CURRENT_TIMESTAMP - INTERVAL 3 WEEK ORDER BY (upvotes - downvotes)  LIMIT 10;")) {

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                ratio = (int) Math.ceil(set.getInt("ratio"));
                if (ratio < BotConstants.RATIO) {
                    ratio = BotConstants.RATIO;
                }
            }
            set.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BotInstance.getJda().getPresence().setActivity(Activity.watching("for " + PopularHandler.ratio + " net upvotes"));
    }
}
