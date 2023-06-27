package com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion;

import com.diamondfire.suggestionsbot.sys.suggestion.impl.flags.FlagSet;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.user.User;

public interface SuggestionSite {
    
    User getAuthor();
    
    String getContents();
    
    FlagSet<?> getFlagSet();
    
    SuggestionType getType();
    
}
