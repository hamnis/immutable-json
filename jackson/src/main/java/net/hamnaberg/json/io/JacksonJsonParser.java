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
    protected Json.JValue parseImpl(Reader reader) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(reader);
        return convert(tree);
    }

    private Json.JValue convert(JsonNode tree) {
        if (tree.isObject()) {
            return convertObject(tree);
        } else if (tree.isArray()) {
            return convertArray(tree);
        } else if (tree.isTextual()) {
            return Json.jString(tree.asText());
        } else if (tree.isNumber()) {
            return Json.jNumber(tree.decimalValue());
        } else if (tree.isBoolean()) {
            return Json.jBoolean(tree.booleanValue());
        }
        return Json.jNull();
    }

    private Json.JArray convertArray(JsonNode tree) {
        Stream<JsonNode> stream = iteratorToFiniteStream(tree.elements());
        return Json.jArray(stream.map(this::convert).collect(Collectors.toList()));
    }

    private Json.JObject convertObject(JsonNode tree) {
        Stream<Map.Entry<String, JsonNode>> stream = iteratorToFiniteStream(tree.fields());
        return Json.jObject(
                stream.map(entry -> entry(entry.getKey(), convert(entry.getValue()))).collect(Collectors.toList())
        );

    }

    static Map.Entry<String, Json.JValue> entry(String name, Json.JValue value) {
        return new AbstractMap.SimpleImmutableEntry<>(name, value);
    }

    static <A> Stream<A> iteratorToFiniteStream(Iterator<A> it) {
        return iterableToFiniteStream(() -> it);
    }

    static <A> Stream<A> iterableToFiniteStream(Iterable<A> it) {
        return StreamSupport.stream(it.spliterator(), false);
    }
}
