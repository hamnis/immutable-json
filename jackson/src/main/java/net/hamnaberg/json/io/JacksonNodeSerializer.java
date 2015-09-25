package net.hamnaberg.json.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.hamnaberg.json.Json;

import java.util.Optional;

public class JacksonNodeSerializer implements JsonSerializer<JsonNode> {
    private JsonNodeFactory factory = JsonNodeFactory.instance;

    @Override
    public JsonNode toJson(Json.JValue value) {
        return value.fold(
                js -> factory.textNode(js.value),
                js -> factory.booleanNode(js.value),
                js -> factory.numberNode(js.value),
                js -> {
                    ObjectNode node = factory.objectNode();
                    js.forEach((k, v) -> node.set(k, toJson(v)));
                    return node;
                },
                js -> {
                    ArrayNode node = factory.arrayNode();
                    js.forEach(v -> node.add(toJson(v)));
                    return node;
                },
                factory::nullNode
        );
    }
}
