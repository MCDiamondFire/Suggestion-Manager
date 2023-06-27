package com.diamondfire.suggestionsbot.sys.suggestion.source.discord;

import com.diamondfire.suggestionsbot.sys.suggestion.impl.user.User;
import net.dv8tion.jda.api.entities.Member;

public class DiscordUser implements User {
    
    private final Member member;
    
    public DiscordUser(Member member) {
        this.member = member;
    }
    
    @Override
    public String getName() {
        return member.getEffectiveName();
    }
    
    @Override
    public String getAvatarUrl() {
        return member.getUser().getEffectiveAvatarUrl();
    }
    
    @Override
    public String toString() {
        return "DiscordUser{" +
                "member=" + member +
                '}';
    }
}
