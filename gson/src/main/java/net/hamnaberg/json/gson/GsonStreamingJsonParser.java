package net.hamnaberg.json.gson;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import io.vavr.control.Try;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.io.JsonParseException;
import net.hamnaberg.json.io.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class GsonStreamingJsonParser extends JsonParser {
    private Gson gson = new Gson();

    @Override
    protected Try<Json.JValue> parseImpl(Reader dataReader) {
        JsonReader jsonReader = gson.newJsonReader(dataReader);
        return Try.of(() -> {
            while (jsonReader.hasNext()) {
                JsonToken token = jsonReader.peek();
                if (isObject(token)) {
                    return parseObject(jsonReader);
                } else if (isArray(token)) {
                    return parseArray(jsonReader);
                } else if (isScalarValue(token)) {
                    return parseScalarValue(token, jsonReader);
                }
            }
            throw new JsonParseException("Nothing parsed");
        });
    }

    private Json.JValue parseScalarValue(JsonToken token, JsonReader reader) throws IOException {
        switch (token) {
            case STRING:
                return Json.jString(reader.nextString());
            case NUMBER:
                return Json.jNumber(new BigDecimal(reader.nextString()));
            case BOOLEAN:
                return Json.jBoolean(reader.nextBoolean());
            case NULL:
                reader.nextNull();
                return Json.jNull();
            default:
                throw new JsonParseException("Not a scalar value");
        }
    }

    private Json.JArray parseArray(JsonReader reader) throws IOException {
        ArrayList<Json.JValue> list = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (isObject(token)) {
                list.add(parseObject(reader));
            } else if (isArray(token)) {
                list.add(parseArray(reader));
            } else if (isScalarValue(token)) {
                list.add(parseScalarValue(token, reader));
            }
        }
        reader.endArray();

        return Json.jArray(list);
    }

    private Json.JObject parseObject(JsonReader reader) throws IOException {
        Map<String, Json.JValue> map = new LinkedHashMap<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            JsonToken token = reader.peek();
            if (isObject(token)) {
                map.put(name, parseObject(reader));
            } else if (isArray(token)) {
                map.put(name, parseArray(reader));
            } else if (isScalarValue(token)) {
                map.put(name, parseScalarValue(token, reader));
            }
        }
        reader.endObject();

        return Json.jObject(map);
    }

    private boolean isObject(JsonToken token) {
        return token == JsonToken.BEGIN_OBJECT;
    }

    private boolean isArray(JsonToken token) {
        return token == JsonToken.BEGIN_ARRAY;
    }

    private boolean isScalarValue(JsonToken token) {
        switch (token) {
            case STRING:
            case NUMBER:
            case BOOLEAN:
            case NULL:
                return true;
            default:
                return false;
        }
    }
}
