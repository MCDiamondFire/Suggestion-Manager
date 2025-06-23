package com.diamondfire.suggestionsbot.util.config.type;

import com.google.gson.annotations.SerializedName;

public class ConfigGuild {

    @SerializedName("discussion_channel")
    private long discussionChannel;
    @SerializedName("popular_channel")
    private long popularChannel;
    @SerializedName("log_channel")
    private long logChannel;

    public long getDiscussionChannel() {
        return this.discussionChannel;
    }

    public long getPopularChannel() {
        return this.popularChannel;
    }

    public long getLogChannel() {
        return this.logChannel;
    }

}
