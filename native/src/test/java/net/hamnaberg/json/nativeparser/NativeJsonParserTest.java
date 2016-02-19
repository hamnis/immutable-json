package net.hamnaberg.json.nativeparser;

import net.hamnaberg.json.io.JsonParser;
import net.hamnaberg.json.io.JsonParserAbstractTest;

public class NativeJsonParserTest extends JsonParserAbstractTest {
    @Override
    protected JsonParser getParser() {
        return new NativeJsonParser();
    }
}
