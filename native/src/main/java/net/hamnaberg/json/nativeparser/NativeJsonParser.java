package net.hamnaberg.json.nativeparser;

import net.hamnaberg.json.Json;
import net.hamnaberg.json.io.IOUtils;
import net.hamnaberg.json.io.JsonParseException;
import net.hamnaberg.json.io.JsonParser;
import org.javafp.parsecj.Reply;

import java.io.Reader;

public class NativeJsonParser extends JsonParser {
    @Override
    protected Json.JValue parseImpl(Reader reader) throws Exception {
        String input = IOUtils.toString(reader);
        Reply<Character, Json.JValue> reply = Grammar.parse(input);
        return reply.match(Reply.Ok::getResult, err -> {
            throw new JsonParseException(err.getMsg());
        });
    }
}
