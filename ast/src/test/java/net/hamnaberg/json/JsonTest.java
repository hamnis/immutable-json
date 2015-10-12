package net.hamnaberg.json;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.Assert.*;

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
        assertEquals(Optional.empty(), empty.headOption());
        assertEquals(0, empty.size());
        assertEquals(1, notEmpty.size());
        assertEquals(Optional.of(Json.jString("Hello")), notEmpty.headOption());
        assertEquals(1, notEmpty.getListAsStrings().size());
        assertEquals(0, notEmpty.getListAsObjects().size());
        assertEquals(0, notEmpty.getListAsBigDecimals().size());
        assertEquals(1, allTheThings.getListAsBigDecimals().size());

        Json.JArray remove = allTheThings.remove(2);
        assertEquals(7, allTheThings.size());
        assertEquals(6, remove.size());
        assertEquals(2, notEmpty.insert(1, Json.jArray(jsonRrange(1, 10))).size());
        assertEquals(11L, Stream.concat(notEmpty.stream(), Json.jArray(jsonRrange(1, 10)).stream()).count());
    }

    @Test
    public void jObject() throws Exception {
        Json.JObject single = Json.jObject("k", Json.jNumber(23));
        assertEquals(single, Json.jObject(new HashMap<String, Json.JValue>() {{
            put("k", Json.jNumber(23));
        }}));
        assertEquals(1, single.size());
        assertEquals(1, single.values().size());
        assertEquals(Collections.singletonList(Json.jNumber(23)), new ArrayList<>(single.values()));
        assertTrue(Json.jEmptyObject().isEmpty());
        assertFalse(Json.jEmptyObject().containsKey("Hello"));
        assertTrue(single.containsKey("k"));
        assertTrue(single.containsValue(Json.jNumber(23)));
        assertEquals(single.mapToList(this::entry), Collections.singletonList(entry("k", Json.jNumber(23))));
        assertEquals(single.put("v", Json.jEmptyArray()), Json.jObject(new HashMap<String, Json.JValue>() {{
            put("k", Json.jNumber(23));
            put("v", Json.jEmptyArray());
        }}));
    }

    public <K, V> Map.Entry<K, V> entry(K k, V v) {
        return new AbstractMap.SimpleImmutableEntry<>(k, v);
    }

    private List<Json.JValue> jsonRrange(int start, int end) {
        ArrayList<Json.JValue> list = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            list.add(Json.jNumber(i));
        }
        return list;
    }
}
