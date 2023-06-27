package com.diamondfire.suggestionsbot.sys.suggestion.impl.flags;

public interface FlagSet<T extends Flag> extends Iterable<T> {

    void addFlag(T flag);
    
    void removeFlag(T flag);
    
    boolean hasFlag(T flag);
    
    T getTopMostFlag();
    
}
