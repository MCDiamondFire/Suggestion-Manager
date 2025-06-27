package com.diamondfire.suggestionsbot.command.impl;

import com.diamondfire.suggestionsbot.command.BotCommand;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.suggestions.suggestion.replies.reference.types.PopularReference;
import net.dv8tion.jda.api.entities.Guild;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.discord.jda5.JDAInteraction;
import org.jetbrains.annotations.NotNull;

public class PopularCommand implements BotCommand {

    @Override
    public Permission getPermission() {
        return Permission.MOD;
    }

    @Command("popular <suggestion>")
    @CommandDescription("Forces a suggestion to become popular.")
    public void command(final @NotNull JDAInteraction interaction, @Argument(value = "suggestion", parserName = "suggestion") Suggestion suggestion) {
        Guild guild = interaction.guild();
        if (guild == null) {
            return;
        }
        suggestion.getReferenceManager().newReference(new PopularReference(guild.getId()));

    }

}
