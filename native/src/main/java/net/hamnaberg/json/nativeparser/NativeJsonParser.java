package net.hamnaberg.json.nativeparser;

import io.vavr.control.Try;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.io.IOUtils;
import net.hamnaberg.json.io.JsonParseException;
import net.hamnaberg.json.io.JsonParser;
import org.javafp.parsecj.Reply;

import java.io.Reader;

public class NativeJsonParser extends JsonParser {
    @Override
    protected Try<Json.JValue> parseImpl(Reader reader) {
        return Try.of(() -> {
            String input = IOUtils.toString(reader);

            Reply<Character, Json.JValue> reply = Grammar.parse(input);
            return reply.match(Reply.Ok::getResult, err -> {
                throw new JsonParseException(err.getMsg());
            });
        });
    }
}
