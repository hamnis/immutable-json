package net.hamnaberg.json.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.hamnaberg.json.*;

import java.io.Reader;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JacksonJsonParser extends JsonParser {
    @Override
    protected JsonValue parseImpl(Reader reader) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(reader);
        return convert(tree);
    }

    private JsonValue convert(JsonNode tree) {
        if (tree.isObject()) {
            return convertObject(tree);
        } else if (tree.isArray()) {
            return convertArray(tree);
        } else if (tree.isTextual()) {
            return JsonString.of(tree.asText());
        } else if (tree.isNumber()) {
            return JsonNumber.of(tree.decimalValue());
        } else if (tree.isBoolean()) {
            return JsonBoolean.of(tree.booleanValue());
        }
        return JsonNull.INSTANCE;
    }

    private JsonArray convertArray(JsonNode tree) {
        Stream<JsonNode> stream = iteratorToFiniteStream(tree.elements());
        return new JsonArray(stream.map(this::convert).collect(Collectors.toList()));
    }

    private JsonObject convertObject(JsonNode tree) {
        Stream<Map.Entry<String, JsonNode>> stream = iteratorToFiniteStream(tree.fields());
        return JsonObject.of(
                stream.map(entry -> entry(entry.getKey(), convert(entry.getValue()))).collect(Collectors.toList())
        );

    }

    static Map.Entry<String, JsonValue> entry(String name, JsonValue value) {
        return new AbstractMap.SimpleImmutableEntry<>(name, value);
    }

    static <A> Stream<A> iteratorToFiniteStream(Iterator<A> it) {
        return iterableToFiniteStream(() -> it);
    }

    static <A> Stream<A> iterableToFiniteStream(Iterable<A> it) {
        return StreamSupport.stream(it.spliterator(), false);
    }
}
