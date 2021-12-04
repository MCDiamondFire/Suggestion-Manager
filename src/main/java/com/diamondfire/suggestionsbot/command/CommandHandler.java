package com.diamondfire.suggestionsbot.command;


import com.diamondfire.suggestionsbot.SuggestionsBot;
import com.diamondfire.suggestionsbot.command.impl.BotCommand;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.command.permissions.Permissions;
import com.diamondfire.suggestionsbot.command.permissions.PermissionHandler;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class CommandHandler extends ListenerAdapter {

    private static HashMap<String, BotCommand> commandMap = new HashMap<>();

    public void register(BotCommand... commands) {
        for (BotCommand command : commands) {
            commandMap.put(command.getName(), command);
        }

        JDA jda = BotInstance.getJda();
        jda.updateCommands().queue(); // empty global commands

        List<Guild> guildList = SuggestionsBot.config.GUILDS.stream()
                .map(jda::getGuildById)
                .collect(Collectors.toList());

        for (Guild guild : guildList) {
            guild
                    .updateCommands()
                    .addCommands(commandMap.values().stream()
                            .map(command -> command.createCommand().setDefaultEnabled(Permission.isPrivileged(command.getPermission())))
                            //.map(command -> command.createCommand().setDefaultEnabled(true))
                            .collect(Collectors.toList()))

                    .queue(commands1 -> {
                        System.out.println("Commands registered. Setting up permissions...");
                        Map<String, Collection<? extends CommandPrivilege>> privilegeMap = new HashMap<>();

                        for (Command command : commands1) {
                            BotCommand command1 = commandMap.get(command.getName());
                            List<CommandPrivilege> commandPrivileges = new ArrayList<>();

                            if (command1.getPermission().getRole() != null) {
                                for (Permission perm : Permissions.ALL) {
                                    if (perm.getRole() != null) {
                                        if (command1.getPermission().getPermissionLevel() <= perm.getPermissionLevel()) {
                                            commandPrivileges.add(CommandPrivilege.enableRole(perm.getRole()));
                                        }
                                    }
                                }
                            }
                            privilegeMap.put(command.getId(), commandPrivileges);
                        }

                        guild.updateCommandPrivileges(privilegeMap).queue(stringListMap -> {
                            System.out.println("Slash command permissions registered.");
                        });
                    });
        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        BotCommand command = commandMap.get(event.getName());

        if (command == null) {
            event.reply("Error executing command.").queue();
            return;
        }

        Permission permission = PermissionHandler.getPermission(Objects.requireNonNull(event.getMember()));
        if (permission.index < commandMap.get(event.getName()).getPermission().index) {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("No Permission!")
                    .setDescription("Sorry, you do not have permission to use this command. Commands that you are able to use are listed in ?help.")
                    .setFooter("Permission Required: " + command.getPermission().getName())
                    .build())
                    .queue();
            return;
        }

        try {
            command.run(event);
        } catch (Exception e) {
            System.err.println("Error in command: " + command.getName());
            e.printStackTrace();

            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("Error Occurred!")
                    .setDescription(e.getMessage())
                    .build())
                    .setEphemeral(true)
                    .queue();
        }
    }

    public HashMap<String, BotCommand> getCommandMap() {
        return commandMap;
    }
}
