package net.hamnaberg.json.io;

import javaslang.control.Either;
import javaslang.control.Left;
import javaslang.control.Option;
import javaslang.control.Right;
import net.hamnaberg.json.Json;

import java.io.*;
import java.nio.charset.StandardCharsets;

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

    public Option<Json.JValue> parseOpt(InputStream is) {
        return parse(is).right().toOption();
    }

    public Option<Json.JValue> parseOpt(String string) {
        return parse(string).right().toOption();
    }

    public Option<Json.JValue> parseOpt(Reader reader) {
        return parse(reader).right().toOption();
    }

    protected abstract Json.JValue parseImpl(Reader reader) throws Exception;
}
