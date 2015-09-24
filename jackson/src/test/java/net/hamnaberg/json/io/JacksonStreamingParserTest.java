package net.hamnaberg.json.io;

public class JacksonStreamingParserTest extends JsonParserAbstractTest {
    @Override
    protected JsonParser getParser() {
        return new JacksonStreamingParser();
    }
}
