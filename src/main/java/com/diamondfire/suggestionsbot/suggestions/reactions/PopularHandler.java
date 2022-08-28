package com.diamondfire.suggestionsbot.suggestions.reactions;

import com.diamondfire.suggestionsbot.database.SingleQueryBuilder;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.util.BotConstants;
import net.dv8tion.jda.api.entities.Activity;

public class PopularHandler {

    public static int ratio;

    public static void calculate() {

        // The algorithm works by counting how many popular suggestions were made in the past 2 weeks,
        // Then adds them to a base minimum value. The value is then clamped.
        new SingleQueryBuilder().query("SELECT COUNT(*) AS recents FROM suggestions WHERE popular_message > 0 " +
                        "AND date > CURRENT_TIMESTAMP - INTERVAL 2 WEEK;")
                .onQuery((set) -> {
                    ratio = BotConstants.MIN_RATIO + (int) set.getInt("recents"));
                    ratio = Math.max(BotConstants.MIN_RATIO, Math.min(ratio, BotConstants.MAX_RATIO));
                }).execute();
        BotInstance.getJda().getPresence().setActivity(Activity.watching("for " + PopularHandler.ratio + " net upvotes"));

    }
}