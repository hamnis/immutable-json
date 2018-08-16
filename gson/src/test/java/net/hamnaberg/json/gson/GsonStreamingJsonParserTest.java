package net.hamnaberg.json.gson;

import net.hamnaberg.json.io.JsonParser;
import net.hamnaberg.json.io.JsonParserAbstractTest;

public class GsonStreamingJsonParserTest extends JsonParserAbstractTest {
    @Override
    protected JsonParser getParser() {
        return new GsonStreamingJsonParser();
    }
}
