package com.diamondfire.suggestionsbot.sys.suggestion.source.discord.flag;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.flags.Flag;
import net.dv8tion.jda.api.entities.Emote;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public enum DiscordFlag implements Flag {
    
    OWEN(830146847644450826L, "owen", 0x0000FF, Priority.HIGH, true),
    
    PATCHED(612434701927448604L, "patched", 0x42C757, Priority.HIGH, true),
    ACCEPT(612434584109580328L, "accepted", 0x4CCD6A, Priority.HIGH, true),
    ACCEPT_ALT(637063533363724291L, "acceptI", 0x7053FF, Priority.HIGH, true),
    
    DENIED(612438944876855349L, "denied", 0xFE5253, Priority.HIGH, true),
    DUPE(612445225134194699L, "dupe", 0xF66E45, Priority.MEDIUM, true),
    
    DISCUSSION(612447965004693541L, "discussion", 0xD95997, Priority.LOW),
    IMPOSSIBLE(612432567051878425L, "impossible", 0x854DF6, Priority.LOW),
    NOT_DF(612421502075404288L, "notdf", 0x854DF6, Priority.LOW),
    POSSIBLE(612436265530294337L, "pos", 0xFF5353, Priority.LOW),
    CONFIRMED_ISSUE(696873088612040706L, "frm_iss", 0xFFC843, Priority.LOW),
    ;
    
    private final long id;
    private final String identifier;
    private final Color color;
    private final int priority;
    private final boolean popularBlocker;
    
    DiscordFlag(long id, String identifier, int color, int priority) {
        this(id, identifier, color, priority, false);
    }
    
    DiscordFlag(long id, String identifier, int color, int priority, boolean popularBlocker) {
        this.id = id;
        this.identifier = identifier;
        this.color = new Color(color);
        this.priority = priority;
        this.popularBlocker = popularBlocker;
    }
    
    public static DiscordFlag getFlag(long idLong) {
        for (DiscordFlag flag : DiscordFlag.values()) {
            if (flag.id == idLong) {
                return flag;
            }
        }
        
        return null;
    }
    
    public long getId() {
        return id;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    @Override
    public String getName() {
        return identifier;
    }
    
    @Nullable
    public Color getColor() {
        return color;
    }
    
    public Emote getEmote() {
        return DiscordInstance.getJda().getEmoteById(getId());
    }
    
    public int getPriority() {
        return priority;
    }
    
    public boolean isPopularBlocker() {
        return popularBlocker;
    }
    
    interface Priority {
        
        int LOW = 0;
        int MEDIUM = 1;
        int HIGH = 2;
    }
    
    @Override
    public String toString() {
        return "DiscordFlag{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", color=" + color +
                ", priority=" + priority +
                ", popularBlocker=" + popularBlocker +
                '}';
    }
}
