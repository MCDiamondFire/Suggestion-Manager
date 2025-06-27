package com.diamondfire.suggestionsbot.guild;

import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.util.config.type.ConfigGuild;
import com.diamondfire.suggestionsbot.util.config.type.ConfigPermission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@NullMarked
public class BotGuild {

    private final Guild guild;
    private final @Nullable TextChannel discussionChannel;
    private final @Nullable TextChannel popularChannel;
    private final @Nullable TextChannel logChannel;
    private final Map<Long, Permission> permissions = new HashMap<>();
    private final Map<Long, @Nullable Permission> userPermissions = new HashMap<>();

    public BotGuild(ConfigGuild config) {
        Guild nullableGuild = BotInstance.getJda().getGuildById(config.getId());
        if (nullableGuild == null) {
            throw new IllegalArgumentException("Guild ID " + config.getId() + " is null");
        }
        this.guild = nullableGuild;
        this.discussionChannel = BotInstance.getJda().getTextChannelById(config.getDiscussionChannel());
        this.popularChannel = BotInstance.getJda().getTextChannelById(config.getPopularChannel());
        this.logChannel = BotInstance.getJda().getTextChannelById(config.getLogChannel());

        for (ConfigPermission configPermission : config.getPermissions()) {
            this.permissions.put(configPermission.getId(), new Permission(configPermission));
        }
    }

    public @Nullable Permission getPermission(Member member) {
        if (this.userPermissions.containsKey(member.getIdLong())) {
            return this.userPermissions.get(member.getIdLong());
        }

        Permission permission = member.getRoles().stream()
                .filter(role -> this.permissions.containsKey(role.getIdLong()))
                .map(role -> this.permissions.get(role.getIdLong()))
                .max(Comparator.comparingInt(Permission::getLevel))
                .orElse(null);

        this.userPermissions.put(member.getIdLong(), permission);

        return permission;
    }

    public int getPermissionLevel(Member member) {
        Permission permission = this.getPermission(member);
        if (permission == null) {
            return 0;
        }
        return permission.getLevel();
    }

    public int getPermissionLevel(User user) {
        Member member = this.guild.getMember(user);
        if (member == null) {
            return -1;
        }
        Permission permission = this.getPermission(member);
        if (permission == null) {
            return 0;
        }
        return permission.getLevel();
    }

    public Guild getGuild() {
        return this.guild;
    }

    public @Nullable TextChannel getDiscussionChannel() {
        return this.discussionChannel;
    }

    public @Nullable TextChannel getPopularChannel() {
        return this.popularChannel;
    }

    public @Nullable TextChannel getLogChannel() {
        return this.logChannel;
    }

    public Map<Long, Permission> getPermissions() {
        return this.permissions;
    }

}
