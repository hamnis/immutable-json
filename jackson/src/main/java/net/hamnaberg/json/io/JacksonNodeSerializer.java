package net.hamnaberg.json.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.hamnaberg.json.JsonArray;
import net.hamnaberg.json.JsonObject;
import net.hamnaberg.json.JsonValue;

import java.util.Optional;

public class JacksonNodeSerializer implements JsonSerializer<JsonNode> {
    private JsonNodeFactory factory = JsonNodeFactory.instance;

    @Override
    public JsonNode toJson(JsonValue value) {
        if (value.isObject()) {
            JsonObject object = value.asJsonObject().get();
            ObjectNode node = factory.objectNode();
            object.forEach((k, v) -> node.set(k, toJson(v)));
            return node;
        }
        else if (value.isArray()) {
            JsonArray jsonArray = value.asJsonArray().get();
            ArrayNode node = factory.arrayNode();
            jsonArray.forEach(v -> node.add(toJson(v)));
            return node;
        }
        else if (value.isString()) {
            Optional<String> s = value.asString();
            return factory.textNode(s.get());
        }
        else if (value.isNumber()) {
            return factory.numberNode(value.asBigDecimal().get());
        }
        else if (value.isBoolean()) {
            return factory.booleanNode(value.asBoolean().get());
        }
        return factory.nullNode();
    }
}
