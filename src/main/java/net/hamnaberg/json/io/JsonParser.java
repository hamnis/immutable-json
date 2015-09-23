package net.hamnaberg.json.io;

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
        try {
            return new Success<>(parseImpl(reader));
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    protected abstract JsonValue parseImpl(Reader reader) throws Exception;
}
