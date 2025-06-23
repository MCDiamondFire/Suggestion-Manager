package com.diamondfire.suggestionsbot.events;

import com.diamondfire.suggestionsbot.BotInstance;
import com.diamondfire.suggestionsbot.command.permissions.Permission;
import com.diamondfire.suggestionsbot.command.permissions.PermissionHandler;
import com.diamondfire.suggestionsbot.suggestions.channels.ChannelHandler;
import com.diamondfire.suggestionsbot.suggestions.channels.SuggestionsChannel;
import com.diamondfire.suggestionsbot.suggestions.reactions.FlagReaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.Reaction;
import com.diamondfire.suggestionsbot.suggestions.reactions.ReactionHandler;
import com.diamondfire.suggestionsbot.suggestions.suggestion.Suggestion;
import com.diamondfire.suggestionsbot.util.Util;
import com.diamondfire.suggestionsbot.util.config.ConfigLoader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionEvent extends ListenerAdapter {

    private static final int EMBED_BODY_MAX_LENGTH = 256;

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        EmojiUnion reactionEmote = event.getReaction().getEmoji();
        if (event.getUser() == null || event.getUser().isBot() || !(reactionEmote instanceof CustomEmoji)) {
            return;
        }


        //  Prevents self reacting
        // TODO Figure out a cleaner implementation for this.

        Message message = event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete();
        SuggestionsChannel suggestionsChannel = ChannelHandler.getSuggestionsChannelOrNull(message.getChannel().getIdLong());
        Reaction reaction = ReactionHandler.getReaction(reactionEmote);

        if (suggestionsChannel == null || reaction == null) {
            return;
        }

        if (reaction.isNetScore()) {
            if (message.getAuthor().getIdLong() == event.getUser().getIdLong()) {
                message.removeReaction(reactionEmote, event.getUser()).queue();
                return;
            }
        }

        Suggestion suggestion = Suggestion.deepFind(message);

        // If we cannot find the suggestion, assume it is a legacy suggestion and try to add it.
        if (suggestion == null) {
            Suggestion legacySuggestion = new Suggestion(message);
            legacySuggestion.getDatabaseManager().addToDatabase();
            return;
        }

        if (!suggestion.isValid()) {
            return;
        }

        suggestion.getSuggestionsChannel().onSuggestionReaction(suggestion, event);

        User user = event.getUser();
        Message suggestionMSG = suggestion.getSuggestion();

        //TODO Cleaner implementation for this, maybe some kind of new "throwaway" reference.
        if (reaction instanceof FlagReaction flag && ReactionHandler.isFirst(message, reactionEmote)) {

            String postName = suggestionsChannel.getPostName();
            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor(user.getName(), null, user.getEffectiveAvatarUrl());
            builder.setTitle(reactionEmote.getFormatted() + " **|** " + String.format("%s %s", postName, flag.getName()));
            builder.setDescription(String.format("[%s](%s) posted by %s was %s by %s.\n", postName, suggestionMSG.getJumpUrl(), suggestionMSG.getAuthor().getAsMention(), flag.getName().toLowerCase(), user.getAsMention()));
            builder.setColor(flag.getColor());
            builder.addField("\u200b", Util.trim(suggestionMSG.getContentRaw(), EMBED_BODY_MAX_LENGTH), false);

            TextChannel channel = BotInstance.getJda().getTextChannelById(ConfigLoader.getConfig().getGuilds().get(message.getGuildId()).getLogChannel());
            if (channel == null) {
                return;
            }
            channel.sendMessageEmbeds(builder.build()).queue();
        }

    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        EmojiUnion reactionEmote = event.getEmoji();
        Member member = event.getGuild().retrieveMemberById(event.getUserId()).complete();

        if (member.getUser().isBot() || !(reactionEmote instanceof CustomEmoji)) {
            return;
        }

        Message message = event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete();
        Suggestion suggestion = Suggestion.deepFind(message);

        if (suggestion == null) {
            return;
        }

        if (PermissionHandler.getPermission(member).getPermissionLevel() >= Permission.MOD.getPermissionLevel() && ReactionHandler.getReaction(reactionEmote) != null) {
            suggestion.getReferenceManager().removeReaction(reactionEmote);
            suggestion.getReferenceManager().plainRefresh();
        }

    }

}
