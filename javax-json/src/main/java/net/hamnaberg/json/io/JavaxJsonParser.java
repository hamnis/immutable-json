package net.hamnaberg.json.io;

import net.hamnaberg.json.Json;

import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.json.stream.JsonParser;

public class JavaxJsonParser extends net.hamnaberg.json.io.JsonParser {
    @Override
    protected Json.JValue parseImpl(Reader reader) throws Exception {
        JsonParser parser = javax.json.Json.createParser(reader);
        JsonParser.Event event;
        while ((event = parser.next()) != null ) {
            if (event == JsonParser.Event.START_OBJECT) {
                return handleObject(parser);
            }
            else if (event == JsonParser.Event.START_ARRAY) {
                return handleArray(parser);
            }
            else if (isScalar(event)) {
                return handleScalarValue(event, parser);
            }
        }
        throw new IllegalStateException("Nothing parsed...");
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
        switch (event) {
            case VALUE_STRING:
            case VALUE_NUMBER:
            case VALUE_TRUE:
            case VALUE_FALSE:
            case VALUE_NULL:
                return true;
            default:
                return false;
        }
    }

    private Json.JArray handleArray(JsonParser parser) {
        JsonParser.Event event;
        List<Json.JValue> list = new ArrayList<>();
        while((event = parser.next())!= JsonParser.Event.END_ARRAY) {
            if (isScalar(event)) {
                list.add(handleScalarValue(event, parser));
            }
            else if (event == JsonParser.Event.START_OBJECT) {
                list.add(handleObject(parser));
            }
            else if (event == JsonParser.Event.START_ARRAY) {
                list.add(handleArray(parser));
            }
        }
        return Json.jArray(list);
    }

    private Json.JObject handleObject(JsonParser parser) {
        Map<String, Json.JValue> map = new LinkedHashMap<>();
        JsonParser.Event event;
        String name = null;
        while((event = parser.next()) != JsonParser.Event.END_OBJECT) {
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
