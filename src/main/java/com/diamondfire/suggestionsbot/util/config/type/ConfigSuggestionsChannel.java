package com.diamondfire.suggestionsbot.util.config.type;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigSuggestionsChannel {

    private long id;
    @SerializedName("post_name")
    private String postName;
    @SerializedName("can_go_popular")
    private boolean canGoPopular;
    private List<String> emojis;

    public long getId() {
        return this.id;
    }

    public String getPostName() {
        return this.postName;
    }

    public boolean isCanGoPopular() {
        return this.canGoPopular;
    }

    public List<String> getEmojis() {
        return this.emojis;
    }

}
