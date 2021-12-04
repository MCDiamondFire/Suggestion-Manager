package com.diamondfire.suggestionsbot.command.permissions;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class Permissions {
    public static final Permission DEVELOPER = new Permission("DEVELOPER", 999, 891001151573614663L);
    public static final Permission MODERATOR = new Permission("MODERATOR", 999, 891001159337259049L);
    public static final Permission TESTER = new Permission("TESTER", 2, 891001203255816203L);
    public static final Permission USER = new Permission("USER", 1, null);

    public static final Permission[] ALL = new Permission[] {
            DEVELOPER,
            MODERATOR,
            TESTER,
            MODERATOR,
    };

    public static Collection<Permission> providingPermissions(Permission permission) {
        return Arrays.stream(ALL)
                .filter(toCheck -> toCheck.index >= permission.index)
                .collect(Collectors.toList());
    }

    public static boolean isPrivileged(Permission permission) {
        return permission.index > Permissions.USER.index;
    }
}
