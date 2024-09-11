package net.hamnaberg.json.glassfish;

import net.hamnaberg.json.io.JsonParser;
import net.hamnaberg.json.io.JsonParserAbstractTest;

public class GlassfishStreamingJsonParserTest extends JsonParserAbstractTest {
    @Override
    protected JsonParser getParser() {
        return new GlassfishJsonParser();
    }
}
