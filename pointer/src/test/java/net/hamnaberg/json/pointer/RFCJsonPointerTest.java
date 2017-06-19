package net.hamnaberg.json.pointer;

import io.vavr.control.Option;
import net.hamnaberg.json.*;
import org.junit.Test;


import static org.junit.Assert.*;

public class RFCJsonPointerTest {


    private Json.JObject json = Json.jObject(
            Json.tuple("foo", Json.jArray(Json.jString("bar"), Json.jString("baz"))),
            Json.tuple("", Json.jNumber(0)),
            Json.tuple("a/b", Json.jNumber(1)),
            Json.tuple("c%d", Json.jNumber(2)),
            Json.tuple("e^f", Json.jNumber(3)),
            Json.tuple("g|h", Json.jNumber(4)),
            Json.tuple("i\\j", Json.jNumber(5)),
            Json.tuple("k\"l", Json.jNumber(6)),
            Json.tuple(" ", Json.jNumber(7)),
            Json.tuple("m~n", Json.jNumber(8))
    );

    @Test
    public void emptyStringselectWholeDocument() {
        Option<Json.JValue> select = JsonPointer.compile("").select(json);
        assertTrue(select.isDefined());
        assertSame(json, select.get());
    }

    @Test
    public void slashFooFindsArray() {
        Option<Json.JValue> select = JsonPointer.compile("/foo").select(json);
        assertTrue(select.isDefined());
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
        find("/m~0n", 8);
    }

    private void find(String pattern, int value) {
        Option<Json.JValue> select = JsonPointer.compile(pattern).select(json);
        assertTrue(select.isDefined());
        assertEquals(Json.jNumber(value), select.get());
    }
}
