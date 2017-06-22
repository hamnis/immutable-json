package net.hamnaberg.json.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonToken;
import io.vavr.control.Try;
import net.hamnaberg.json.*;
import net.hamnaberg.json.io.JsonParseException;
import net.hamnaberg.json.io.JsonParser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public final class JacksonStreamingParser extends JsonParser {
    private final JsonFactory factory = new JsonFactory();

    @Override
    protected Try<Json.JValue> parseImpl(Reader reader) {
        return Try.of(() -> {
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
            throw new JsonParseException("Nothing parsed");
        });
    }

    private Json.JObject handleObject(com.fasterxml.jackson.core.JsonParser parser) throws Exception {
        LinkedHashMap<String, Json.JValue> map = new LinkedHashMap<>();
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
        return Json.jObject(map);
    }

    private Json.JValue handleArray(com.fasterxml.jackson.core.JsonParser parser) throws Exception {
        JsonToken token;
        List<Json.JValue> values = new ArrayList<>();
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
        return Json.jArray(values);
    }

    private Json.JValue handleScalarValue(com.fasterxml.jackson.core.JsonParser parser) throws Exception {
        JsonToken token = parser.getCurrentToken();
        if (token == JsonToken.VALUE_STRING) {
            return Json.jString(parser.getValueAsString());
        }
        else if (token.isNumeric()) {
            return Json.jNumber(parser.getDecimalValue());
        }
        else if (token.isBoolean()) {
            return Json.jBoolean(parser.getBooleanValue());
        }
        else {
            return Json.jNull();
        }
    }

}
