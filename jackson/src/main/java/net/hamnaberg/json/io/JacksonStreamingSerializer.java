package net.hamnaberg.json.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import javaslang.control.Try;
import net.hamnaberg.json.JsonArray;
import net.hamnaberg.json.JsonObject;
import net.hamnaberg.json.JsonValue;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.function.Consumer;

public class JacksonStreamingSerializer implements JsonSerializer<Consumer<OutputStream>> {
    private JsonFactory factory = new JsonFactory();

    @Override
    public Consumer<OutputStream> toJson(JsonValue value) {
        return os -> {
            Try<Void> run = Try.run(() -> write(value, os));
            run.onFailure(t -> {
                throw new RuntimeException(t);
            });
        };
    }

    private void write(JsonValue value, OutputStream os) throws Exception{
        JsonGenerator generator = factory.createGenerator(os, JsonEncoding.UTF8);
        write(value, generator);
        generator.close();
    }

    private void write(JsonValue value, JsonGenerator generator) throws IOException {
        if (value.isObject()) {
            generator.writeStartObject();
            JsonObject object = value.asJsonObjectOrEmpty();
            for (Map.Entry<String, JsonValue> kv : object) {
                JsonValue v = kv.getValue();
                generator.writeFieldName(kv.getKey());
                write(v, generator);
            }
            generator.writeEndObject();
        }
        else if (value.isArray()) {
            generator.writeStartArray();
            JsonArray jsonValues = value.asJsonArrayOrEmpty();
            for (JsonValue v : jsonValues) {
                write(v, generator);
            }
            generator.writeEndArray();
        }
        else if (value.isBoolean()) {
            generator.writeBoolean(value.asJsonBoolean().get().value);
        }
        else if (value.isString()) {
            generator.writeString(value.asString().get());
        }
        else if (value.isNumber()) {
            generator.writeNumber(value.asBigDecimal().get());
        }
        else if (value.isNull()) {
            generator.writeNull();
        }
    }
}
