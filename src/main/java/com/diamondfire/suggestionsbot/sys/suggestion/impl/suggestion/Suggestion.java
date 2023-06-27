package com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion;

import com.diamondfire.suggestionsbot.sys.suggestion.impl.flags.*;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.user.User;
import com.diamondfire.suggestionsbot.sys.suggestion.source.SuggestionSource;

public class Suggestion<T extends SuggestionSite> implements SuggestionSite {
    
    private final long id;
    private final T site;
    
    private final SuggestionSource<T> source;
    
    public Suggestion(long id, T site, SuggestionSource<T> source) {
        this.id = id;
        this.site = site;
        this.source = source;
    }
    
    public long getId() {
        return id;
    }
    
    @Override
    public User getAuthor() {
        return site.getAuthor();
    }
    
    @Override
    public String getContents() {
        return site.getContents();
    }
    
    @Override
    public FlagSet<? extends Flag> getFlagSet() {
        return site.getFlagSet();
    }
    
    @Override
    public SuggestionType getType() {
        return site.getType();
    }
    
    public T getSite() {
        return site;
    }
    
    public void refreshSites() {
        source.updateSite(this);
    }
}
