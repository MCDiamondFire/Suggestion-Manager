package com.diamondfire.suggestionsbot.events;

import com.diamondfire.suggestionsbot.SuggestionsBot;
import com.diamondfire.suggestionsbot.command.permissions.Permissions;
import com.diamondfire.suggestionsbot.command.permissions.PermissionHandler;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.channels.ChannelHandler;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionHandler;
import com.diamondfire.suggestionsbot.suggestions.reactions.flag.ReactionFlag;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReactionEvent extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {

        MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();
        if (event.getUser().isBot() || !reactionEmote.isEmote()) return;

        boolean selfReacted = false;
        Message message = event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete();

        if (reactionEmote.getIdLong() == SuggestionsBot.config.UPVOTE || reactionEmote.getIdLong() == SuggestionsBot.config.DOWNVOTE) {
            if (message.getAuthor().getIdLong() == event.getUser().getIdLong()) {
                message.removeReaction(event.getReactionEmote().getEmote(), event.getUser()).queue();
                selfReacted = true;
            }
        }


        Suggestion suggestion = Suggestion.deepFind(message);

        // If we cannot find the suggestion, assume it is a legacy suggestion and try to add it.
        if (suggestion == null && ChannelHandler.isValidChannel(message.getChannel().getIdLong())) {
            Suggestion legacySuggestion = new Suggestion(message);
            legacySuggestion.databaseManager.addToDatabase();
            return;
        }

        if (selfReacted) {
            return;
        }

        if (suggestion.isValid()) {
            suggestion.getChannel().onSuggestionReaction(suggestion, event);

            Reaction reaction = ReactionHandler.getReaction(reactionEmote.getIdLong());
            User user = event.getUser();
            Message suggestionMSG = suggestion.getSuggestion();

            if (reaction instanceof ReactionFlag) {
                ReactionFlag flag = (ReactionFlag) reaction;
                if (ReactionHandler.isFirst(message, reactionEmote.getEmote())) {
                    String channelName = suggestion.getChannel().getName();
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setAuthor(user.getName(), null, user.getEffectiveAvatarUrl());
                    builder.setTitle(reactionEmote.getEmote().getAsMention() + " **|** " + String.format("%s %s", channelName, flag.getEmbedName()));
                    builder.setDescription(String.format("[%s](%s) posted by %s was %s by %s.\n", channelName, suggestionMSG.getJumpUrl(), suggestionMSG.getAuthor().getAsMention(), flag.getEmbedName().toLowerCase(), user.getAsMention()));
                    builder.setColor(flag.getColor());
                    builder.addField("\u200b", Util.trim(suggestionMSG.getContentRaw(), 256), false);

                    BotInstance.getJda().getTextChannelById(SuggestionsBot.config.REACTION_LOG).sendMessageEmbeds(builder.build()).queue();
                }
            }
        }
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();
        Member member = event.getGuild().retrieveMemberById(event.getUserId()).complete();

        if (!member.getUser().isBot() && !reactionEmote.isEmote()) return;

        Message message = event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete();
        Suggestion suggestion = Suggestion.deepFind(message);

        if (PermissionHandler.getPermission(member).getPermissionLevel() >= Permissions.MODERATOR.getPermissionLevel() && ReactionHandler.getReaction(reactionEmote.getIdLong()) != null) {
            suggestion.referenceManager.removeReaction(reactionEmote);
            suggestion.referenceManager.plainRefresh();
        }
    }
}




