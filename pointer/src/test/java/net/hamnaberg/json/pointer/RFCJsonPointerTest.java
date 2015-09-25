package net.hamnaberg.json.pointer;

import net.hamnaberg.json.*;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class RFCJsonPointerTest {
    private static Map.Entry<String, Json.JValue> entry(String name, Json.JValue value) {
        return new AbstractMap.SimpleImmutableEntry<>(name, value);
    }

    private Json.JObject json = Json.jObject(
            entry("foo", Json.jArray(Json.jString("bar"), Json.jString("baz"))),
            entry("", Json.jNumber(0)),
            entry("a/b", Json.jNumber(1)),
            entry("c%d", Json.jNumber(2)),
            entry("e^f", Json.jNumber(3)),
            entry("g|h", Json.jNumber(4)),
            entry("i\\j", Json.jNumber(5)),
            entry("k\"l", Json.jNumber(6)),
            entry(" ", Json.jNumber(7)),
            entry("m~n", Json.jNumber(8))
    );

    @Test
    public void emptyStringselectWholeDocument() {
        Optional<Json.JValue> select = JsonPointer.compile("").select(json);
        assertTrue(select.isPresent());
        assertSame(json, select.get());
    }

    @Test
    public void slashFooFindsArray() {
        Optional<Json.JValue> select = JsonPointer.compile("/foo").select(json);
        assertTrue(select.isPresent());
        assertEquals(Json.jArray(Json.jString("bar"), Json.jString("baz")), select.get());
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
        Optional<Json.JValue> select = JsonPointer.compile(pattern).select(json);
        assertTrue(select.isPresent());
        assertEquals(Json.jNumber(value), select.get());
    }
}
