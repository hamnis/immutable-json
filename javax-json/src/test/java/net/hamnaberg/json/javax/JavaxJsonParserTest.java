package net.hamnaberg.json.javax;

import net.hamnaberg.json.io.JsonParser;
import net.hamnaberg.json.io.JsonParserAbstractTest;

public class JavaxJsonParserTest extends JsonParserAbstractTest {

    @Override
    protected JsonParser getParser() {
        return new JavaxJsonParser();
    }
}
