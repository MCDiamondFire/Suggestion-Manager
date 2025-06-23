package com.diamondfire.suggestionsbot.suggestions.reactions;


import com.diamondfire.suggestionsbot.suggestions.reactions.flag.accept.Accept;
import com.diamondfire.suggestionsbot.suggestions.reactions.flag.accept.OtherAccept;
import com.diamondfire.suggestionsbot.suggestions.reactions.flag.accept.Patched;
import com.diamondfire.suggestionsbot.suggestions.reactions.flag.denied.Denied;
import com.diamondfire.suggestionsbot.suggestions.reactions.flag.denied.Duplicate;
import com.diamondfire.suggestionsbot.suggestions.reactions.misc.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReactionHandler {

    private static final HashMap<Long, Reaction> idList = new HashMap<>();
    private static final HashMap<String, Reaction> identityList = new HashMap<>();

    static {
        register(
                new Accept(),
                new OtherAccept(),
                new Patched(),

                new Denied(),
                new Duplicate(),

                new Discussion(),
                new Impossible(),
                new Possible(),
                new NotDF(),


                new ConfirmedIssue()

        );

    }

    public static void register(Reaction... reactions) {
        for (Reaction reaction : reactions) {
            idList.put(reaction.getID(), reaction);
        }
        for (Reaction reaction : reactions) {
            identityList.put(reaction.getIdentifier(), reaction);
        }
    }

    public static Reaction getReaction(long id) {
        return idList.get(id);
    }

    public static Reaction getFromIdentifier(String name) {
        return identityList.get(name);
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
                .anyMatch((reactionEmotes) -> reactionEmotes.getEmoji().asCustom().getIdLong() == emote.asCustom().getIdLong() && reactionEmotes.getCount() == 1);
    }


}
