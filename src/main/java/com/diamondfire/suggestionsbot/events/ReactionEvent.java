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
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ReactionEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {

        MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();
        if (event.getUser().isBot() || !reactionEmote.isEmote()) return;


        //  Prevents self reacting
        // TODO Figure out a cleaner implementation for this.

        boolean selfReacted = false;
        Message message = event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete();

        if (reactionEmote.getIdLong() == BotConstants.UPVOTE || reactionEmote.getIdLong() == BotConstants.DOWNVOTE) {
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

            //TODO Cleaner implementation for this, maybe some kind of new "throwaway" reference.
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

                    BotInstance.getJda().getTextChannelById(BotConstants.REACTION_LOG).sendMessage(builder.build()).queue();

                    // Dupe warning system (Posts to watched users when at 7+ dupes with high dupe ratio) - Added by Electrosolt
                    if (reaction instanceof Duplicate) {
                        String dupeID = reaction.getIdentifier();

                        // Reaction Stat getter, copied from the Stats command. All values used to determine warn-worthiness.
                        new SingleQueryBuilder().query("SELECT * from suggestions WHERE author_id = ?", (statement -> {
                            statement.setLong(1, message.getAuthor().getIdLong());
                        })).onQuery((table) -> {
                            int suggestionCount = 0;
                            int upVotes = 0;
                            int downVotes = 0;
                            int dupeCount = 0;
                            ArrayList<String> reactions = new ArrayList<>();

                            do {
                                suggestionCount++;
                                upVotes += table.getInt("upvotes");
                                downVotes += table.getInt("downvotes");
                                reactions.addAll(Arrays.asList(table.getString("special_reactions").split(",")));

                            } while (table.next());
                            HashMap<Reaction, Integer> reactionStat = new HashMap<>();

                            for (String reactName : reactions) {
                                Reaction reaction = ReactionHandler.getFromIdentifier(reactName);
                                if (reaction == null) {
                                    continue;
                                }
                                if (reactName.equals(dupeID)){
                                    dupeCount++;
                                }
                                if (!reactionStat.containsKey(reaction)) {
                                    reactionStat.put(reaction, 1);
                                } else {
                                    int i = reactionStat.get(reaction) + 1;
                                    reactionStat.replace(reaction, i);
                                }

                            }

                            // Determine whether to and send the warning message.
                            // Has disabled functionality for ignoring warns if over a certain upvote threshold.
                            if (dupeCount >= 7) {
                                float dupeRatio = dupeCount / (float)suggestionCount;
                                //float upvoteRatio = upVotes / ((float)downVotes + 1.0f);
                                if (dupeRatio > 0.1){
                                    //if (!(upvoteRatio > 6 && upvotes > 500))
                                    builder.clear(); // Reuse Embed Builder from log message
                                    builder.setAuthor(suggestionMSG.getAuthor().getName(), null, suggestionMSG.getAuthor().getEffectiveAvatarUrl())
                                    builder.setTitle(reactionEmote.getEmote().getAsMention() + " **|** Duplicate Warning");
                                    builder.setDescription(String.format("%s has reached %d duplicates (%.1f% of submissions).", suggestionMSG.getAuthor().getAsMention(), dupeCount, dupeRatio));
                                    builder.setColor(flag.getColor());
                                    BotInstance.getJda().getTextChannelById(BotConstants.WATCHED_USERS).sendMessage(builder.build()).queue();
                                }
                            }

                        }).onNotFound(() -> {
                            throw new IllegalStateException("Suggestion author somehow has no suggestions.");
                        }).execute();

                    }
                }
            }
        }

    }

    @Override
    public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent event) {
        MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();
        Member member = event.getGuild().retrieveMemberById(event.getUserId()).complete();

        if (!member.getUser().isBot() && !reactionEmote.isEmote()) return;

        Message message = event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete();
        Suggestion suggestion = Suggestion.deepFind(message);

        if (PermissionHandler.getPermission(member).getPermissionLevel() >= Permission.MOD.getPermissionLevel() && ReactionHandler.getReaction(reactionEmote.getIdLong()) != null) {
            suggestion.referenceManager.removeReaction(reactionEmote);
            suggestion.referenceManager.plainRefresh();
        }


    }


}




