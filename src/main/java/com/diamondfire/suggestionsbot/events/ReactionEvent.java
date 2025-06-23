package com.diamondfire.suggestionsbot.events;

import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.command.permissions.PermissionHandler;
import com.diamondfire.suggestionsbot.suggestions.channels.ChannelHandler;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.util.Util;
import com.diamondfire.suggestionsbot.instance.BotInstance;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionHandler;
import com.diamondfire.suggestionsbot.suggestions.reactions.flag.ReactionFlag;
import com.diamondfire.suggestionsbot.util.BotConstants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionEvent extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        EmojiUnion reactionEmote = event.getReaction().getEmoji();
        if (event.getUser() == null || event.getUser().isBot() || !(reactionEmote instanceof CustomEmoji)) {
            return;
        }


        //  Prevents self reacting
        // TODO Figure out a cleaner implementation for this.

        boolean selfReacted = false;
        Message message = event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete();

        if (reactionEmote.asCustom().getIdLong() == BotConstants.UPVOTE || reactionEmote.asCustom().getIdLong() == BotConstants.DOWNVOTE) {
            if (message.getAuthor().getIdLong() == event.getUser().getIdLong()) {
                message.removeReaction(reactionEmote, event.getUser()).queue();
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

            Reaction reaction = ReactionHandler.getReaction(reactionEmote.asCustom().getIdLong());
            User user = event.getUser();
            Message suggestionMSG = suggestion.getSuggestion();

            //TODO Cleaner implementation for this, maybe some kind of new "throwaway" reference.
            if (reaction instanceof ReactionFlag) {
                ReactionFlag flag = (ReactionFlag) reaction;
                if (ReactionHandler.isFirst(message, reactionEmote)) {
                    String channelName = suggestion.getChannel().getName();
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setAuthor(user.getName(), null, user.getEffectiveAvatarUrl());
                    builder.setTitle(reactionEmote.getFormatted() + " **|** " + String.format("%s %s", channelName, flag.getEmbedName()));
                    builder.setDescription(String.format("[%s](%s) posted by %s was %s by %s.\n", channelName, suggestionMSG.getJumpUrl(), suggestionMSG.getAuthor().getAsMention(), flag.getEmbedName().toLowerCase(), user.getAsMention()));
                    builder.setColor(flag.getColor());
                    builder.addField("\u200b", Util.trim(suggestionMSG.getContentRaw(), 256), false);

                    BotInstance.getJda().getTextChannelById(BotConstants.REACTION_LOG).sendMessageEmbeds(builder.build()).queue();
                }
            }
        }

    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        EmojiUnion reactionEmote = event.getEmoji();
        Member member = event.getGuild().retrieveMemberById(event.getUserId()).complete();

        if (!member.getUser().isBot() && !(reactionEmote instanceof CustomEmoji)) return;

        Message message = event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete();
        Suggestion suggestion = Suggestion.deepFind(message);

        if (PermissionHandler.getPermission(member).getPermissionLevel() >= Permission.MOD.getPermissionLevel() && ReactionHandler.getReaction(reactionEmote.asCustom().getIdLong()) != null) {
            suggestion.referenceManager.removeReaction(reactionEmote);
            suggestion.referenceManager.plainRefresh();
        }


    }


}




