package com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion;

public enum SuggestionType {
    
    GENERAL_SUGGESTION,
    CODE_SUGGESTION,
    
    ISSUE,
    BETA_ISSUE,
    DANGEROUS_ISSUE,
    UNDEFINED, // Used when the suggestion type is not yet defined.
    ;
}
