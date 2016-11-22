package net.hamnaberg.json.io;

import net.hamnaberg.json.Json;
import net.hamnaberg.json.PrettyPrinter;

import java.io.*;
import java.nio.charset.StandardCharsets;

public enum JsonSerializer {
    ;

    public static void write(Json.JValue value, OutputStream stream) {
        write(value, new OutputStreamWriter(stream, StandardCharsets.UTF_8));
    }

    public static String writeToString(Json.JValue value, PrettyPrinter pretty) {
        return value.pretty(pretty);
    }

    public static String writeToString(Json.JValue value) {
        return writeToString(value, PrettyPrinter.nospaces());
    }

    public static void write(Json.JValue value, Writer writer) {
        write(value, writer, PrettyPrinter.nospaces());
    }

    public static void write(Json.JValue value, Writer writer, PrettyPrinter printer) {
        final BufferedWriter buffer = (writer instanceof BufferedWriter) ? ((BufferedWriter)writer) : new BufferedWriter(writer);
        try {
            buffer.write(value.pretty(printer));
        } catch (IOException e) {
            throw new JsonWriteException(e);
        }
    }
}
