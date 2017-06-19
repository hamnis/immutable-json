package net.hamnaberg.json.io;

import io.vavr.control.Option;
import net.hamnaberg.json.Json;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public abstract class JsonParser {
    public final Json.JValue parse(InputStream is) {
        return parse(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)));
    }

    public final Json.JValue parse(String string) {
        return parse(new StringReader(string));
    }

    public final Json.JValue parse(Reader reader) {
        BufferedReader buf;
        if (reader instanceof BufferedReader) {
            buf = (BufferedReader) reader;
        } else {
            buf = new BufferedReader(reader);
        }

        try (Reader r = buf) {
            return parseImpl(r);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public Option<Json.JValue> parseOpt(InputStream is) {
        return parseOpt(is, this::parse);
    }

    public Option<Json.JValue> parseOpt(String string) {
        return parseOpt(string, this::parse);
    }

    public Option<Json.JValue> parseOpt(Reader reader) {
        return parseOpt(reader, this::parse);
    }

    private <A> Option<Json.JValue> parseOpt(A input, Function<A, Json.JValue> f) {
        try {
            return Option.of(f.apply(input));
        } catch (JsonParseException e) {
            return Option.none();
        }

    }

    protected abstract Json.JValue parseImpl(Reader reader) throws Exception;
}
