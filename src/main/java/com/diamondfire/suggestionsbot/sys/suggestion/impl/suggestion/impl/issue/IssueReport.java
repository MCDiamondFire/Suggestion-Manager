package com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.impl.issue;

import com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.SuggestionSite;

public interface IssueReport extends SuggestionSite {
    
    boolean isConfirmed();
    
    boolean wontFix();
    
    boolean isInvalid();

}
