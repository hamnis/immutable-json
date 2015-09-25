package net.hamnaberg.json.io;


import net.hamnaberg.json.Json;
import org.glassfish.json.JsonFactory;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.util.Map;

public final class JavaxJsonNodeSerializer implements JsonSerializer<JsonValue> {
    @Override
    public JsonValue toJson(Json.JValue value) {
        return value.fold(
                j -> JsonFactory.jsonString(j.value),
                j -> j.value ? JsonValue.TRUE : JsonValue.FALSE,
                j -> JsonFactory.jsonNumber(j.value),
                j -> {
                    JsonObjectBuilder jb = javax.json.Json.createObjectBuilder();
                    for (Map.Entry<String, Json.JValue> entry : j) {
                        jb.add(entry.getKey(), toJson(entry.getValue()));
                    }
                    return jb.build();
                },
                j -> {
                    JsonArrayBuilder arrayBuilder = javax.json.Json.createArrayBuilder();
                    for (Json.JValue v : j) {
                        arrayBuilder.add(toJson(v));
                    }
                    return arrayBuilder.build();
                },
                () -> JsonValue.NULL
        );
    }
}
