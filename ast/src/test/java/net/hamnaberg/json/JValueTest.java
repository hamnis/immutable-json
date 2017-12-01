package net.hamnaberg.json;

import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class JValueTest {
    @Test
    public void jNumber() {
        Json.JNumber expected = Json.jNumber(new BigDecimal("20"));
        assertEquals(expected, Json.jNumber(20));
        assertEquals(expected, Json.jNumber(20L));
        assertEquals(expected, Json.jNumber(20.0));
        assertTrue(expected.isNumber());
        assertTrue(expected.isScalar());
        assertEquals("20", expected.scalarToString().getOrElse(""));
    }

    @Test
    public void JString() {
        Json.JString string = Json.jString("Hello");
        assertTrue(string.isString());
        assertTrue(string.isScalar());
        assertEquals("Hello", string.scalarToString().getOrElse(""));
    }

    @Test
    public void JNull() {
        Json.JNull nullable = Json.jNull();
        assertTrue(nullable.isNull());
        assertTrue(nullable.isScalar());
        assertFalse(nullable.isString());
        assertEquals("null", nullable.scalarToString().getOrElse(""));
    }

    @Test
    public void JBoolean() {
        Json.JBoolean tru = Json.jBoolean(true);
        Json.JBoolean fals = Json.jBoolean(false);
        assertTrue(tru.value);
        assertFalse(fals.value);
        assertNotEquals(tru, fals);
        assertEquals("true", tru.scalarToString().getOrElse(""));
        assertEquals("false", fals.scalarToString().getOrElse(""));
    }

    @Test
    public void JArray() {
        Json.JArray array1 = Json.jArray(Json.jBoolean(true), Json.jNull());
        Json.JArray array2 = Json.jArray(List.of(Json.jBoolean(true), Json.jNull()));
        assertEquals(array1, array2);
        assertEquals(array1, array1.concat(Json.jEmptyArray()));
        assertTrue(array1.isArray());
        assertTrue(array2.isArray());
        assertEquals(2, array1.size());
        assertEquals(2, array2.size());
        assertEquals(3, array1.append(1).size());
        assertNotEquals(array1, array1.append(1));
        assertNotEquals(array1.prepend(1), array1.append(1));
    }

    @Test
    public void JObject() {
        assertFooObject(Json.jObject("foo", "bar"));
        assertFooObject(Json.jObject("foo", 123));
        assertFooObject(Json.jObject("foo", 123.0));
        assertFooObject(Json.jObject("foo", 123L));
        assertFooObject(Json.jObject("foo", BigDecimal.ONE));
        assertFooObject(Json.jObject("foo", false));
        assertFooObject(Json.jObject("foo", Json.jNull()));
        assertFooObject(Json.jObject(Json.tuple("foo", Json.jNull())));
        assertFooObject(Json.jObject(Json.nullableTuple("foo", Json.jNull())));
        assertFooObject(Json.jObject(Json.nullableTuple("foo", (Json.JValue) null)));
        assertFooObject(Json.jObject(Json.tuple("foo", Option.of(Json.jString("Hello")))));
    }

    private void assertFooObject(Json.JObject object) {
        assertTrue(object.containsKey("foo"));
        assertEquals(object.size(), 1);
        Json.JValue foo = object.get("foo").getOrElse(Json.jNull());
        assertTrue(foo.isScalar());
        assertFalse(object.isScalar());
        assertTrue(object.isObject());
        assertSame(object, object.asJsonObjectOrEmpty());
        assertEquals(Json.jEmptyObject(), foo.asJsonObjectOrEmpty());
    }
}
