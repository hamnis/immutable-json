package net.hamnaberg.json.glassfish;

import net.hamnaberg.json.Json;
import net.hamnaberg.json.io.JsonParseException;

import java.io.Reader;
import java.util.*;

import jakarta.json.stream.JsonParser;

public final class GlassfishJsonParser extends net.hamnaberg.json.io.JsonParser {
    private final EnumSet<JsonParser.Event> scalarSet =
            EnumSet.of(
                    JsonParser.Event.VALUE_STRING,
                    JsonParser.Event.VALUE_NUMBER,
                    JsonParser.Event.VALUE_TRUE,
                    JsonParser.Event.VALUE_FALSE,
                    JsonParser.Event.VALUE_NULL
            );

    @Override
    protected Json.JValue parseImpl(Reader reader) {
        JsonParser parser = jakarta.json.Json.createParser(reader);
        try {
            JsonParser.Event event;

            while ((event = parser.next()) != null) {
                if (event == JsonParser.Event.START_OBJECT) {
                    return handleObject(parser);
                } else if (event == JsonParser.Event.START_ARRAY) {
                    return handleArray(parser);
                } else if (isScalar(event)) {
                    return handleScalarValue(event, parser);
                }
            }
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
        throw new JsonParseException("Nothing parsed");

    }

    private Json.JValue handleScalarValue(JsonParser.Event event, JsonParser parser) {
        switch (event) {
            case VALUE_STRING:
                return Json.jString(parser.getString());
            case VALUE_NUMBER:
                return Json.jNumber(parser.getBigDecimal());
            case VALUE_TRUE:
                return Json.jBoolean(true);
            case VALUE_FALSE:
                return Json.jBoolean(false);
            case VALUE_NULL:
                return Json.jNull();
            default:
                throw new IllegalArgumentException("Not a scalar value " + event);
        }
    }

    private boolean isScalar(JsonParser.Event event) {
        return scalarSet.contains(event);
    }

    private Json.JArray handleArray(JsonParser parser) {
        JsonParser.Event event;
        List<Json.JValue> list = new ArrayList<>();
        while ((event = parser.next()) != JsonParser.Event.END_ARRAY) {
            if (isScalar(event)) {
                list.add(handleScalarValue(event, parser));
            } else if (event == JsonParser.Event.START_OBJECT) {
                list.add(handleObject(parser));
            } else if (event == JsonParser.Event.START_ARRAY) {
                list.add(handleArray(parser));
            }
        }
        return Json.jArray(list);
    }

    private Json.JObject handleObject(JsonParser parser) {
        Map<String, Json.JValue> map = new LinkedHashMap<>();
        JsonParser.Event event;
        String name = null;
        while ((event = parser.next()) != JsonParser.Event.END_OBJECT) {
            if (event == JsonParser.Event.KEY_NAME) {
                name = parser.getString();
            }
            if (name != null) {
                if (isScalar(event)) {
                    map.put(name, handleScalarValue(event, parser));
                } else if (event == JsonParser.Event.START_OBJECT) {
                    map.put(name, handleObject(parser));
                } else if (event == JsonParser.Event.START_ARRAY) {
                    map.put(name, handleArray(parser));
                }
            }
        }
        return Json.jObject(map);
    }
}
