package com.diamondfire.suggestionsbot.command.permissions;

import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.stream.Stream;

public enum Permission {
    DEVELOPER(526182228229619713L, 999),
    MOD(528943220235960321L, 999),
    USER(0L, 1);

    private static final HashMap<Long, Permission> quickMap = new HashMap<>();

    static {
        Stream.of(values()).forEach(permissions -> quickMap.put(permissions.getRole(), permissions));
    }

    private final long role;
    private final int permissionLevel;

    Permission(long roleID, int permissionLevel) {
        this.role = roleID;
        this.permissionLevel = permissionLevel;
    }

    public static Permission fromRole(long roleID) {
        Permission perm = quickMap.get(roleID);
        if (perm == null) {
            return USER;
        }
        return perm;
    }

    public long getRole() {
        return role;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public boolean hasPermission(Member member) {
        return getPermissionLevel() <= PermissionHandler.getPermission(member).getPermissionLevel();
    }
}
