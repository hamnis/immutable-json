package net.hamnaberg.json.io;

import javaslang.control.Either;
import javaslang.control.Left;
import javaslang.control.Right;
import net.hamnaberg.json.Json;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public abstract class JsonParser {
    public Either<Exception, Json.JValue> parse(InputStream is) {
        return parse(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)));
    }

    public Either<Exception, Json.JValue> parse(String string) {
        return parse(new StringReader(string));
    }

    public Either<Exception, Json.JValue> parse(Reader reader) {
        try(Reader r = reader) {
            return new Right<>(parseImpl(r));
        } catch(Exception e) {
            return new Left<>(e);
        }
    }

    public Optional<Json.JValue> parseOpt(InputStream is) {
        return parse(is).right().toJavaOptional();
    }

    public Optional<Json.JValue> parseOpt(String string) {
        return parse(string).right().toJavaOptional();
    }

    public Optional<Json.JValue> parseOpt(Reader reader) {
        return parse(reader).right().toJavaOptional();
    }

    protected abstract Json.JValue parseImpl(Reader reader) throws Exception;
}
