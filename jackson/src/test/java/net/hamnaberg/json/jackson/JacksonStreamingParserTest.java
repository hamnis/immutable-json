package net.hamnaberg.json.jackson;

import net.hamnaberg.json.io.JsonParser;
import net.hamnaberg.json.io.JsonParserAbstractTest;

public class JacksonStreamingParserTest extends JsonParserAbstractTest {
    @Override
    protected JsonParser getParser() {
        return new JacksonStreamingParser();
    }
}
