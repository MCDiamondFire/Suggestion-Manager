package com.diamondfire.suggestionsbot.util.chatfilter;

public abstract class ChatFilter {
    
    final boolean filter(FilterData data) {
        return filterString(data.getSimplifiedMessage(), data);
    }
    
    public abstract boolean filterString(String message, FilterData data);
}