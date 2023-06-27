package com.diamondfire.suggestionsbot.sys.tasks;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;

import java.util.concurrent.*;

public class TaskRegistry {
    
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    
    public void initialize() {
        if (DiscordInstance.getConfig().isDevBot()) return;
        
        register(
        
        );
        
    }
    
    private void register(LoopingTask... tasks) {
        for (LoopingTask task : tasks) {
            scheduledExecutorService.scheduleAtFixedRate(task,
                    task.getInitialStart(),
                    task.getNextLoop(),
                    TimeUnit.MILLISECONDS);
        }
    }
    
    public void schedule(Runnable runnable, long timeMs) {
        scheduledExecutorService.schedule(runnable, timeMs, TimeUnit.MILLISECONDS);
    }
    
    public void schedule(OneTimeTask task) {
        schedule(task, task.getExecution());
    }
    
}
