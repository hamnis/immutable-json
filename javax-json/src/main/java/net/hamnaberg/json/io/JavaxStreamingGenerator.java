package net.hamnaberg.json.io;

import net.hamnaberg.json.Json;
import org.glassfish.json.JsonFactory;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import java.util.function.Consumer;

public final class JavaxStreamingGenerator implements JsonSerializer {

    @Override
    public void write(Json.JValue value, Writer writer) {
        try (JsonGenerator generator = javax.json.Json.createGenerator(writer)) {
            generator.write(convert(value));
        }
    }

    public JsonValue convert(Json.JValue value) {
        return value.fold(
                j -> JsonFactory.jsonString(j.value),
                j -> j.value ? JsonValue.TRUE : JsonValue.FALSE,
                j -> JsonFactory.jsonNumber(j.value),
                j -> {
                    JsonObjectBuilder jb = javax.json.Json.createObjectBuilder();
                    for (Map.Entry<String, Json.JValue> entry : j) {
                        jb.add(entry.getKey(), convert(entry.getValue()));
                    }
                    return jb.build();
                },
                j -> {
                    JsonArrayBuilder arrayBuilder = javax.json.Json.createArrayBuilder();
                    for (Json.JValue v : j) {
                        arrayBuilder.add(convert(v));
                    }
                    return arrayBuilder.build();
                },
                () -> JsonValue.NULL
        );
    }
}
