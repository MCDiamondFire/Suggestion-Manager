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
        Stream.of(values()).forEach(permissions -> quickMap.put(permissions.getRoleId(), permissions));
    }

    private final long roleId;
    private final int permissionLevel;

    Permission(long roleId, int permissionLevel) {
        this.roleId = roleId;
        this.permissionLevel = permissionLevel;
    }

    public static Permission fromRole(long roleId) {
        Permission perm = quickMap.get(roleId);
        if (perm == null) {
            return USER;
        }
        return perm;
    }

    public long getRoleId() {
        return this.roleId;
    }

    public int getPermissionLevel() {
        return this.permissionLevel;
    }

    public boolean hasPermission(Member member) {
        return this.getPermissionLevel() <= PermissionHandler.getPermission(member).getPermissionLevel();
    }

}
