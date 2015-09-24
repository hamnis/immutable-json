package net.hamnaberg.json.io;

import javaslang.control.Try;
import net.hamnaberg.json.JsonValue;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class JsonParser {
    public Try<JsonValue> parse(InputStream is) {
        return parse(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)));
    }

    public Try<JsonValue> parse(String string) {
        return parse(new StringReader(string));
    }

    public Try<JsonValue> parse(Reader reader) {
        return Try.of(() -> parseImpl(reader));
    }

    protected abstract JsonValue parseImpl(Reader reader) throws Exception;
}
