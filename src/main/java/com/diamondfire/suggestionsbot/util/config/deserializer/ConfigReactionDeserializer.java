package com.diamondfire.suggestionsbot.util.config.deserializer;

import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigDownvoteReaction;
import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigFlagReaction;
import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigReaction;
import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigResultReaction;
import com.diamondfire.suggestionsbot.util.config.type.reaction.ConfigUpvoteReaction;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ConfigReactionDeserializer implements JsonDeserializer<ConfigReaction> {

    @Override
    public ConfigReaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("type").getAsString();
        return switch (type) {
            case "normal" -> context.deserialize(obj, ConfigResultReaction.class);
            case "flag" -> context.deserialize(obj, ConfigFlagReaction.class);
            case "upvote" -> context.deserialize(obj, ConfigUpvoteReaction.class);
            case "downvote" -> context.deserialize(obj, ConfigDownvoteReaction.class);
            default -> throw new JsonParseException("Unknown reaction type: " + type);
        };
    }

}
