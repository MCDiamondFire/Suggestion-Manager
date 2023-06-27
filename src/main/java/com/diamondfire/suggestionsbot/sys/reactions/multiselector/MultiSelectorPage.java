package com.diamondfire.suggestionsbot.sys.reactions.multiselector;

import net.dv8tion.jda.api.EmbedBuilder;

public class MultiSelectorPage {
    
    private final String name;
    private final EmbedBuilder page;
    private final boolean hidden;
    private final String customEmote;
    
    public MultiSelectorPage(String name, EmbedBuilder page) {
        this.name = name;
        this.page = page;
        this.hidden = false;
        this.customEmote = null;
    }
    
    public MultiSelectorPage(String name, EmbedBuilder page, boolean hidden) {
        this.name = name;
        this.page = page;
        this.hidden = hidden;
        this.customEmote = null;
    }
    
    public MultiSelectorPage(String name, EmbedBuilder page, String customEmote, boolean hidden) {
        this.name = name;
        this.page = page;
        this.hidden = hidden;
        this.customEmote = customEmote;
    }
    
    public String getName() {
        return name;
    }
    
    public EmbedBuilder getPage() {
        return page;
    }
    
    public boolean isHidden() {
        return hidden;
    }
    
    public String getCustomEmote() {
        return customEmote;
    }
}
