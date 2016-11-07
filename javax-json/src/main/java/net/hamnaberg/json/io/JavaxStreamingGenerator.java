package net.hamnaberg.json.io;

import javaslang.Tuple2;
import net.hamnaberg.json.Json;
import org.glassfish.json.JsonFactory;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;

public final class JavaxStreamingGenerator implements JsonSerializer {
    private final JsonGeneratorFactory factory;

    public JavaxStreamingGenerator(Map<String, ?> config) {
        factory = javax.json.Json.createGeneratorFactory(config);
    }

    public JavaxStreamingGenerator() {
        this(Collections.emptyMap());
    }

    @Override
    public void write(Json.JValue value, Writer writer) {
        try (JsonGenerator generator = factory.createGenerator(writer)) {
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
                    for (Tuple2<String, Json.JValue> entry : j) {
                        jb.add(entry._1, convert(entry._2));
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
