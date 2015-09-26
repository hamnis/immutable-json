package net.hamnaberg.json;

import javaslang.collection.List;
import javaslang.control.Option;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class JsonTest {
    @Test
    public void jString() {
        Json.JString hello = Json.jString("hello");
        assertEquals("hello", hello.getValue());
        assertEquals(hello, Json.jString("hello"));
        assertNotEquals(hello.toString(), Json.jString("hello").value);
    }

    @Test
    public void jNumber() {
        Json.JNumber n1 = Json.jNumber(1);
        assertEquals(n1, Json.jNumber(1L));
        assertEquals(n1, Json.jNumber(new Long(1L)));
        assertEquals(n1, Json.jNumber(new BigDecimal(1)));
        assertEquals(1, n1.asInt());
        assertEquals(1L, n1.asLong());
        assertEquals(1.0, n1.asDouble(), 0.0);
        assertEquals(1.0, Json.jNumber(1.0).asDouble(), 0.0);
    }

    @Test
    public void jBoolean() {
        Json.JBoolean yes = Json.jBoolean(true);
        Json.JBoolean no = Json.jBoolean(false);
        assertEquals(yes, Json.jBoolean(true));
        assertEquals(no, Json.jBoolean(false));
        assertNotEquals(yes.value, no.value);
        assertEquals(yes.value, true);
        assertEquals(no.value, false);
    }

    @Test
    public void jNull() {
        Json.JNull n = Json.jNull();
        Json.JNull n2 = Json.JNull.INSTANCE;
        assertSame(n, n2);
        assertEquals(n, n2);
    }

    @Test
    public void jArray() {
        Json.JArray allTheThings = Json.jArray(
                Arrays.asList(
                        Json.jBoolean(true),
                        Json.jBoolean(false),
                        Json.jString("balle"),
                        Json.jNumber(23),
                        Json.jNull(),
                        Json.jEmptyArray(),
                        Json.jEmptyObject()
                ));

        assertEquals(7, allTheThings.size());

        assertEquals(Json.jBoolean(true), allTheThings.headOption().get());
        Json.JArray empty = Json.jEmptyArray();
        Json.JArray notEmpty = empty.append(Json.jString("Hello"));
        Json.JArray notEmpty2 = empty.append(Json.jString("Hello")).prepend(Json.jBoolean(false));
        assertEquals(Json.jArray(Json.jBoolean(false), Json.jString("Hello")), notEmpty2);
        assertEquals(Option.none(), empty.headOption());
        assertEquals(0, empty.size());
        assertEquals(1, notEmpty.size());
        assertEquals(Option.of(Json.jString("Hello")), notEmpty.headOption());
        assertEquals(1, notEmpty.getListAsStrings().length());
        assertEquals(0, notEmpty.getListAsObjects().length());
        assertEquals(0, notEmpty.getListAsBigDecimals().length());
        assertEquals(1, allTheThings.getListAsBigDecimals().length());

        Json.JArray remove = allTheThings.remove(2);
        assertEquals(7, allTheThings.size());
        assertEquals(6, remove.size());
        assertEquals(3, notEmpty2.insert(1, Json.jArray(List.range(1, 10).map(Json::jNumber))).size());
        assertEquals(11L, Stream.concat(notEmpty2.stream(), Json.jArray(List.range(1, 10).map(Json::jNumber)).stream()).count());
    }
}
