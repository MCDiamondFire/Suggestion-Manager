package com.owen1212055.suggestionsbot.suggestions.reactions;


import com.owen1212055.suggestionsbot.suggestions.reactions.flag.accept.Accept;
import com.owen1212055.suggestionsbot.suggestions.reactions.flag.accept.OtherAccept;
import com.owen1212055.suggestionsbot.suggestions.reactions.flag.accept.Patched;
import com.owen1212055.suggestionsbot.suggestions.reactions.flag.denied.Denied;
import com.owen1212055.suggestionsbot.suggestions.reactions.flag.denied.Duplicate;
import com.owen1212055.suggestionsbot.suggestions.reactions.misc.*;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
                new NotDF(),
                new PriorityMax(),
                new PriorityMid(),
                new PriorityMin()

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
        return message.getReactions().stream()
                .filter((reaction -> reaction.getReactionEmote().isEmote()))
                .map((reaction) -> ReactionHandler.getReaction(reaction.getReactionEmote().getIdLong()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static boolean isFirst(Message message, Emote emote) {
        return message.getReactions().stream()
                .filter(reaction -> reaction.getReactionEmote().isEmote())
                .anyMatch((reactionEmotez) -> reactionEmotez.getReactionEmote().getEmote().equals(emote) && reactionEmotez.getCount() == 1);
    }


}
