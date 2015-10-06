package net.hamnaberg.json.io;

import net.hamnaberg.json.Json;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public interface JsonSerializer {
    default void write(Json.JValue value, OutputStream stream) {
        write(value, new OutputStreamWriter(stream, StandardCharsets.UTF_8));
    }

    default String writeToString(Json.JValue value) {
        StringWriter writer = new StringWriter();
        write(value, writer);
        return writer.toString();
    }

    void write(Json.JValue value, Writer reader);
}
