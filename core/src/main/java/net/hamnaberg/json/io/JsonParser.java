package net.hamnaberg.json.io;

import net.hamnaberg.json.Json;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Function;

public abstract class JsonParser {
    public Json.JValue parse(InputStream is) {
        return parse(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)));
    }

    public Json.JValue parse(String string) {
        return parse(new StringReader(string));
    }

    public Json.JValue parse(Reader reader) {
        try (Reader r = reader) {
            return parseImpl(r);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public Optional<Json.JValue> parseOpt(InputStream is) {
        return parseOpt(is, this::parse);
    }

    public Optional<Json.JValue> parseOpt(String string) {
        return parseOpt(string, this::parse);
    }

    public Optional<Json.JValue> parseOpt(Reader reader) {
        return parseOpt(reader, this::parse);
    }

    private <A> Optional<Json.JValue> parseOpt(A input, Function<A, Json.JValue> f) {
        try {
            return Optional.of(f.apply(input));
        } catch (JsonParseException e) {
            return Optional.empty();
        }

    }

    protected abstract Json.JValue parseImpl(Reader reader) throws Exception;
}
