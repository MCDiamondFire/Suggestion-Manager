package com.diamondfire.suggestionsbot.util.config;

import com.diamondfire.suggestionsbot.util.config.type.ConfigGuild;
import com.diamondfire.suggestionsbot.util.config.type.ConfigSuggestionsChannel;
import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigReaction;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Config {

    private String token;
    @SerializedName("jdbc_url")
    private String jdbcUrl;
    @SerializedName("db_user")
    private String dbUser;
    @SerializedName("db_password")
    private String dbPassword;
    private Map<String, ConfigGuild> guilds;
    private List<ConfigSuggestionsChannel> channels;
    private List<ConfigReaction> reactions;

    public String getToken() {
        return this.token;
    }

    public String getJdbcUrl() {
        return this.jdbcUrl;
    }

    public String getDbUser() {
        return this.dbUser;
    }

    public String getDbPassword() {
        return this.dbPassword;
    }

    public Map<String, ConfigGuild> getGuilds() {
        return this.guilds;
    }

    public List<ConfigSuggestionsChannel> getChannels() {
        return this.channels;
    }

    public List<ConfigReaction> getReactions() {
        return this.reactions;
    }

}
