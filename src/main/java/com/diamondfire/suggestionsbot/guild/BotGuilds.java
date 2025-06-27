package com.diamondfire.suggestionsbot.guild;

import com.diamondfire.suggestionsbot.util.config.ConfigLoader;
import com.diamondfire.suggestionsbot.util.config.type.ConfigGuild;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public final class BotGuilds {

    private static final Map<Long, BotGuild> guilds = new HashMap<>();

    static {
        for (ConfigGuild config : ConfigLoader.getConfig().getGuilds()) {
            guilds.put(config.getId(), new BotGuild(config));
        }
    }

    private BotGuilds() {
    }

    public static BotGuild get(Guild guild) {
        return guilds.get(guild.getIdLong());
    }

    public static BotGuild get(long id) {
        return guilds.get(id);
    }

}
