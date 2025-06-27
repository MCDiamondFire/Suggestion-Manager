package com.diamondfire.suggestionsbot.util.config.type;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigGuild {

    private long id;
    @SerializedName("discussion_channel")
    private long discussionChannel;
    @SerializedName("popular_channel")
    private long popularChannel;
    @SerializedName("log_channel")
    private long logChannel;
    private List<ConfigPermission> permissions;

    public long getId() {
        return this.id;
    }

    public long getDiscussionChannel() {
        return this.discussionChannel;
    }

    public long getPopularChannel() {
        return this.popularChannel;
    }

    public long getLogChannel() {
        return this.logChannel;
    }

    public List<ConfigPermission> getPermissions() {
        return this.permissions;
    }

}
