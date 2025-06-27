package com.diamondfire.suggestionsbot.guild;

import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.util.config.type.ConfigPermission;
import net.dv8tion.jda.api.entities.Role;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Permission {

    private final long id;
    private final int level;
    private final Role jda;

    public Permission(ConfigPermission config) {
        this.id = config.getId();
        this.level = config.getLevel();

        Role role = BotInstance.getJda().getRoleById(this.id);
        if (role == null) {
            throw new IllegalArgumentException("Role ID " + this.id + " is null");
        }
        this.jda = role;
    }

    public long getId() {
        return this.id;
    }

    public int getLevel() {
        return this.level;
    }

    public Role getJda() {
        return this.jda;
    }

}
