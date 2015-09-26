package net.hamnaberg.json.io;

public class JavaxJsonParserTest extends JsonParserAbstractTest {

    @Override
    protected JsonParser getParser() {
        return new JavaxJsonParser();
    }
}
