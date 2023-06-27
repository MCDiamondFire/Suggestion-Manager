package com.diamondfire.suggestionsbot.sys.tasks;

public interface OneTimeTask extends Runnable {
    
    long getExecution();
    
}
