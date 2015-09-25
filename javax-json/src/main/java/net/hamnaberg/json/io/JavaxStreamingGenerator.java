package net.hamnaberg.json.io;

import net.hamnaberg.json.Json;

import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import java.io.OutputStream;
import java.util.function.Consumer;

public final class JavaxStreamingGenerator implements JsonSerializer<Consumer<OutputStream>> {
    private final JavaxJsonNodeSerializer serializer = new JavaxJsonNodeSerializer();

    @Override
    public Consumer<OutputStream> toJson(Json.JValue value) {
        JsonValue converted = serializer.toJson(value);
        return (os) -> {
            try (JsonGenerator generator = javax.json.Json.createGenerator(os)) {
                generator.write(converted);
            }
        };
    }
}
