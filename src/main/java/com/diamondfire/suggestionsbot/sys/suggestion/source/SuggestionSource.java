package com.diamondfire.suggestionsbot.sys.suggestion.source;

import com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.*;
import com.diamondfire.suggestionsbot.util.IdHandler;

public interface SuggestionSource<T extends SuggestionSite> {
    
    String getSourceName();
    
    default Suggestion<T> createSuggestion(T site) {
        return createSuggestion(site, IdHandler.getNewId());
    }
    
    default Suggestion<T> createSuggestion(T site, long id) {
        return new Suggestion<>(id, site, this);
    }
    
    T createSite(Suggestion<?> suggestion);
    
    void updateSite(Suggestion<T> suggestion);
    
    
    void storeSuggestion(Suggestion<T> suggestion);
    
    T getSuggestion(Suggestion<?> site);
    
    boolean hasSuggestion(Suggestion<?> site);
}
