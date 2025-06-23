package com.diamondfire.suggestionsbot.util.config.type.reaction;

import com.google.gson.annotations.SerializedName;

public class ConfigFlagReaction extends ConfigResultReaction {

    private String name;
    @SerializedName("prevent_popular")
    private boolean preventPopular;

    public String getName() {
        return this.name;
    }

    public boolean isPreventPopular() {
        return this.preventPopular;
    }

}
