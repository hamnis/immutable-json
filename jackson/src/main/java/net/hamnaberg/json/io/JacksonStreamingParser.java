package net.hamnaberg.json.io;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonToken;
import net.hamnaberg.json.*;

import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class JacksonStreamingParser extends JsonParser {
    @Override
    protected JsonValue parseImpl(Reader reader) throws Exception {
        JsonFactory factory = new JsonFactory();
        com.fasterxml.jackson.core.JsonParser parser = factory.createParser(reader);
        JsonToken token;
        while ((token = parser.nextToken()) != null ) {
            if (token == JsonToken.START_OBJECT) {
                return handleObject(parser);
            }
            else if (token == JsonToken.START_ARRAY) {
                return handleArray(parser);
            }
            else if (token.isScalarValue()) {
                return handleScalarValue(parser);
            }
        }
        throw new IllegalStateException("Nothing parsed...");
    }

    private JsonObject handleObject(com.fasterxml.jackson.core.JsonParser parser) throws Exception {
        LinkedHashMap<String, JsonValue> map = new LinkedHashMap<>();
        String fieldName;
        while ((fieldName = parser.nextFieldName()) != null) {
            JsonToken token = parser.nextValue();
            if (token.isScalarValue()) {
                map.put(fieldName, handleScalarValue(parser));
            }
            else if (token == JsonToken.START_ARRAY) {
                map.put(fieldName, handleArray(parser));
            }
            else if (token == JsonToken.START_OBJECT) {
                map.put(fieldName, handleObject(parser));
            }
        }
        return JsonObject.of(map);
    }

    private JsonValue handleArray(com.fasterxml.jackson.core.JsonParser parser) throws Exception {
        JsonToken token;
        List<JsonValue> values = new ArrayList<>();
        while ((token = parser.nextToken()) != JsonToken.END_ARRAY) {
            if (token.isScalarValue()) {
                values.add(handleScalarValue(parser));
            }
            else if (token == JsonToken.START_ARRAY) {
                values.add(handleArray(parser));
            }
            else if (token == JsonToken.START_OBJECT) {
                values.add(handleObject(parser));
            }
        }
        return new JsonArray(values);
    }

    private JsonValue handleScalarValue(com.fasterxml.jackson.core.JsonParser parser) throws Exception {
        JsonToken token = parser.getCurrentToken();
        if (token == JsonToken.VALUE_STRING) {
            return JsonString.of(parser.getValueAsString());
        }
        else if (token.isNumeric()) {
            return JsonNumber.of(parser.getDecimalValue());
        }
        else if (token.isBoolean()) {
            return JsonBoolean.of(parser.getBooleanValue());
        }
        else {
            return JsonNull.INSTANCE;
        }
    }

}
