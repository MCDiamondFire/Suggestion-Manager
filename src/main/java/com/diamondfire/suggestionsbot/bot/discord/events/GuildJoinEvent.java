package com.diamondfire.suggestionsbot.bot.discord.events;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildJoinEvent extends ListenerAdapter {
    
    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
    }
}
