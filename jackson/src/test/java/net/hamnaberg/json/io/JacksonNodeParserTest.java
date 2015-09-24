package net.hamnaberg.json.io;

public class JacksonNodeParserTest extends JsonParserAbstractTest {
    @Override
    protected JsonParser getParser() {
        return new JacksonJsonParser();
    }
}
