package com.diamondfire.suggestionsbot.suggestions.reactions;

import com.diamondfire.suggestionsbot.util.config.ConfigLoader;
import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigFlagReaction;
import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigReaction;
import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigResultReaction;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ReactionHandler {

    private static final HashMap<Long, Reaction> emojiMap = new HashMap<>();
    private static final HashMap<String, Reaction> identifierMap = new HashMap<>();

    static {
        for (ConfigReaction config : ConfigLoader.getConfig().getReactions()) {
            if (config instanceof ConfigFlagReaction flagConfig) {
                register(new FlagReaction(flagConfig));
                continue;
            } else if (config instanceof ConfigResultReaction resultReaction) {
                register(new ResultReaction(resultReaction));
                continue;
            }

            register(new Reaction(config));
        }
    }

    private ReactionHandler() {
    }

    public static void register(Reaction... reactions) {
        for (Reaction reaction : reactions) {
            emojiMap.put(reaction.getEmoji(), reaction);
        }
        for (Reaction reaction : reactions) {
            identifierMap.put(reaction.getIdentifier(), reaction);
        }
    }

    public static Reaction getReaction(long id) {
        return emojiMap.get(id);
    }

    public static Reaction getReaction(MessageReaction reaction) {
        return getReaction(reaction.getEmoji());
    }

    public static Reaction getReaction(EmojiUnion emoji) {
        if (emoji instanceof CustomEmoji) {
            return emojiMap.get(emoji.asCustom().getIdLong());
        }
        return null;
    }

    public static Reaction getReaction(String identifier) {
        return identifierMap.get(identifier);
    }

    public static List<Reaction> getReactions(Message message) {
        List<Reaction> reactions = new ArrayList<>();

        for (MessageReaction reaction : message.getReactions()) {
            if (!(reaction instanceof CustomEmoji)) {
                continue;
            }
            Reaction sugReaction = ReactionHandler.getReaction(reaction.getEmoji().asCustom().getIdLong());
            if (sugReaction != null) {
                reactions.add(sugReaction);
            }
        }

        return reactions;
    }

    public static boolean isFirst(Message message, EmojiUnion emote) {
        return message.getReactions().stream()
                .filter(reaction -> reaction.getEmoji() instanceof CustomEmoji)
                .anyMatch(reactionEmotes -> reactionEmotes.getEmoji().asCustom().getIdLong() == emote.asCustom().getIdLong() && reactionEmotes.getCount() == 1);
    }

}
