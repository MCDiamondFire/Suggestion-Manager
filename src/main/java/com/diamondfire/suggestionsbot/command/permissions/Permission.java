package com.diamondfire.suggestionsbot.command.permissions;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.util.Collection;
import java.util.Objects;

public class Permission {
    public final int index;
    public final String name;
    public final Long role;

    Permission(String name, int index, Long role) {
        this.name = name;
        this.index = index;
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, name, role);
    }

    public boolean matches(Member member) {
        return member.getRoles().contains(member.getGuild().getRoleById(getRole()));
    }
    public int getPermissionLevel() {
        return this.index;
    }

    public String getName() {
        return this.name;
    }

    public Long getRole() {
        return this.role;
    }

    public static boolean isPrivileged(Permission permission) {
        return permission.getRole() == null;
    }

    public boolean hasPermission(Member member) {
        return hasPermission(PermissionHandler.getPermission(member));
    }

    public boolean hasPermission(Permission permission) {
        return getPermissionLevel() <= permission.getPermissionLevel();
    }
}
