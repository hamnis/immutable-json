package net.hamnaberg.json.nativeparser;

import io.vavr.control.Try;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.io.IOUtils;
import net.hamnaberg.json.io.JsonParser;

import java.io.Reader;

public class NativeJsonParser extends JsonParser {
    @Override
    protected Try<Json.JValue> parseImpl(Reader reader) {
        return Try.of(() -> {
            String input = IOUtils.toString(reader);
            return JParsecGrammar.parser.parse(input);
        });
    }
}
