package com.diamondfire.suggestionsbot.suggestions.reactions;

import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.util.BotConstants;
import com.diamondfire.suggestionsbot.util.ConnectionProvider;
import net.dv8tion.jda.api.entities.Activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PopularHandler {
    public static int ratio;

    public static void calculate() {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT (COUNT(*) * 0.5) AS ratio FROM suggestions WHERE popular_message > 0 AND date > CURRENT_TIMESTAMP - INTERVAL 3 WEEK ORDER BY (upvotes - downvotes) LIMIT 10;")) {

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                ratio = (int) Math.ceil(set.getInt("ratio")) + BotConstants.RATIO;
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
