package com.diamondfire.suggestionsbot.reactions;

import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.database.SingleQueryBuilder;
import com.diamondfire.suggestionsbot.util.BotConstants;
import net.dv8tion.jda.api.entities.Activity;

public final class PopularHandler {

    private static int ratio;

    private PopularHandler() {
    }

    public static void calculate() {
        new SingleQueryBuilder().query("SELECT (COUNT(*) * 0.5) AS ratio FROM suggestions WHERE popular_message > 0 " +
                        "AND date > CURRENT_TIMESTAMP - INTERVAL 1 WEEK ORDER BY (upvotes - downvotes) LIMIT 10;")
                .onQuery(set -> {
                    ratio = (int) (double) set.getInt("ratio") + BotConstants.RATIO;
                    if (ratio < BotConstants.RATIO) {
                        ratio = BotConstants.RATIO;
                    }
                }).execute();
        BotInstance.getJda().getPresence().setActivity(Activity.watching("for " + PopularHandler.ratio + " net upvotes"));
    }

    public static int getRatio() {
        return ratio;
    }

}
