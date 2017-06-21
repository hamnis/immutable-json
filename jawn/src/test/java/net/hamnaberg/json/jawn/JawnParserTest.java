package net.hamnaberg.json.jawn;

import net.hamnaberg.json.io.JsonParser;
import net.hamnaberg.json.io.JsonParserAbstractTest;

public class JawnParserTest extends JsonParserAbstractTest {
    @Override
    protected JsonParser getParser() {
        return new JawnParser();
    }
}
