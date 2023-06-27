package com.diamondfire.suggestionsbot.bot.discord.events;

import com.diamondfire.suggestionsbot.sys.reactions.impl.ReactionHandler;
import com.diamondfire.suggestionsbot.sys.suggestion.impl.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.sys.suggestion.source.SuggestionSources;
import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.*;
import com.diamondfire.suggestionsbot.sys.suggestion.source.discord.flag.DiscordFlag;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.react.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ReactionEvent extends ListenerAdapter {
    
    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        if (event.getMember().getUser().isBot()) return;
        
        if (ReactionHandler.isMessageReserved(event.getMessageIdLong())) {
            event.getReaction().retrieveUsers().queue((users) -> {
                if (!users.contains(event.getJDA().getSelfUser()) || !ReactionHandler.isWaiting(event.getMember().getIdLong())) {
                    if (event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_MANAGE)) {
                        MessageReaction.ReactionEmote emote = event.getReactionEmote();
                        if (emote.isEmoji()) {
                            event.getChannel().removeReactionById(event.getMessageIdLong(), emote.getEmoji(), event.getUser()).queue();
                        } else if (emote.isEmote()) {
                            event.getChannel().removeReactionById(event.getMessageIdLong(), emote.getEmote(), event.getUser()).queue();
                        }
                        
                    }
                } else {
                    if (ReactionHandler.isWaiting(event.getMember().getIdLong())) {
                        ReactionHandler.handleReaction(event);
                    }
                }
            });
        }
        
        
        new Thread(() -> {
            Suggestion<DiscordSite> fetchedSuggestion = DiscordSuggestionStorage.INSTANCE.getSuggestion(event.getMessageIdLong());
            if (fetchedSuggestion == null) {
                return;
            }
            
            fetchedSuggestion.getSite().getFlagSet().addFlag(DiscordFlag.getFlag(event.getReactionEmote().getIdLong()));
            SuggestionSources.DISCORD.updateSite(fetchedSuggestion);
        }).start();
    }
    
    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        //if (event.getUser().isBot()) return;
        
        new Thread(() -> {
            Suggestion<DiscordSite> fetchedSuggestion = DiscordSuggestionStorage.INSTANCE.getSuggestion(event.getMessageIdLong());
            if (fetchedSuggestion == null) {
                return;
            }
            System.out.println(fetchedSuggestion.getSite());
            fetchedSuggestion.getSite().getFlagSet().removeFlag(DiscordFlag.getFlag(event.getReactionEmote().getIdLong()));
            SuggestionSources.DISCORD.updateSite(fetchedSuggestion);
        }).start();
    }
}
