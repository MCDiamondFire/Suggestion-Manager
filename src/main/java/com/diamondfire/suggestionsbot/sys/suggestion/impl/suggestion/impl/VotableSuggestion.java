package com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.impl;

import com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.*;

public interface VotableSuggestion extends SuggestionSite {
    
    int getUpvotes();
    
    int getDownvotes();
    
    default int getNetUpvotes() {
        return getUpvotes() - getDownvotes();
    }
    
}
