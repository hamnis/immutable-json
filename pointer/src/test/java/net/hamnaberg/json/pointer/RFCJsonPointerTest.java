package net.hamnaberg.json.pointer;

import net.hamnaberg.json.*;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class RFCJsonPointerTest {
    private static Map.Entry<String, JsonValue> entry(String name, JsonValue value) {
        return new AbstractMap.SimpleImmutableEntry<>(name, value);
    }

    private JsonObject json = JsonObject.of(
            entry("foo", JsonArray.of(new JsonString("bar"), new JsonString("baz"))),
            entry("", JsonNumber.of(0)),
            entry("a/b", JsonNumber.of(1)),
            entry("c%d", JsonNumber.of(2)),
            entry("e^f", JsonNumber.of(3)),
            entry("g|h", JsonNumber.of(4)),
            entry("i\\j", JsonNumber.of(5)),
            entry("k\"l", JsonNumber.of(6)),
            entry(" ", JsonNumber.of(7)),
            entry("m~n", JsonNumber.of(8))
    );

    @Test
    public void emptyStringselectWholeDocument() {
        Optional<JsonValue> select = JsonPointer.compile("").select(json);
        assertTrue(select.isPresent());
        assertSame(json, select.get());
    }

    @Test
    public void slashFooFindsArray() {
        Optional<JsonValue> select = JsonPointer.compile("/foo").select(json);
        assertTrue(select.isPresent());
        assertEquals(JsonArray.of(new JsonString("bar"), new JsonString("baz")), select.get());
    }

    @Test
    public void findNumbers() {
        find("/",     0);
        find("/a~1b", 1);
        find("/c%d",  2);
        find("/e^f",  3);
        find("/g|h",  4);
        find("/i\\j", 5);
        find("/k\"l", 6);
        find("/ ",    7);
        find("m~n",   8);
    }

    private void find(String pattern, int value) {
        Optional<JsonValue> select = JsonPointer.compile(pattern).select(json);
        assertTrue(select.isPresent());
        assertEquals(JsonNumber.of(value), select.get());
    }
}
