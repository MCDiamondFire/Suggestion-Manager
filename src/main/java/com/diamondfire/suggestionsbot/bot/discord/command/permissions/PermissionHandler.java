package com.diamondfire.suggestionsbot.bot.discord.command.permissions;

import com.diamondfire.suggestionsbot.bot.discord.DiscordInstance;
import net.dv8tion.jda.api.entities.Member;

import java.util.Comparator;

public class PermissionHandler {
    
    
    public static Permission getPermission(Member member) {
        // Return user if guild isn't df guild.
        if (member.getGuild().getIdLong() != DiscordInstance.DF_GUILD) {
            return Permission.USER;
        }
        
        //Calculates the highest permission that the member has access to.
        Permission perm = member.getRoles().stream()
                .map((role) -> Permission.fromRole(role.getIdLong()))
                .max(Comparator.comparingInt(Permission::getPermissionLevel))
                .orElse(Permission.USER);
        
        return perm;
    }
    
}
