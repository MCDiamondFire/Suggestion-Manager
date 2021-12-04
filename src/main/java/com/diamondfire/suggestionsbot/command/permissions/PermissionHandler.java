package com.diamondfire.suggestionsbot.command.permissions;

import net.dv8tion.jda.api.entities.Member;

public class PermissionHandler {

    public static Permission getPermission(Member member) {
        for (Permission permission : Permissions.ALL) {
            if (permission.matches(member)) return permission;
        }

        return Permissions.USER;
    }
}
