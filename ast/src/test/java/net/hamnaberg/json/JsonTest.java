package net.hamnaberg.json;

import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class JsonTest {
    @Test
    public void jString() {
        Json.JString hello = Json.jString("hello");
        assertEquals("hello", hello.getValue());
        assertEquals(hello, Json.jString("hello"));
        assertNotEquals(hello.toString(), Json.jString("hello").value());
    }

    @Test
    public void jNumber() {
        Json.JNumber n1 = Json.jNumber(1);
        assertEquals(n1, Json.jNumber(1L));
        assertEquals(n1, Json.jNumber(Long.valueOf(1L)));
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
        assertNotEquals(yes.value(), no.value());
        assertTrue(yes.value());
        assertFalse(no.value());
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
        assertEquals(2, notEmpty.insert(1, Json.jArray(jsonRange(1, 10))).size());
        assertEquals(11L, Stream.concat(notEmpty.stream(), Json.jArray(jsonRange(1, 10)).stream()).count());
    }

    @Test
    public void jObject() throws Exception {
        Json.JObject single = Json.jObject("k", Json.jNumber(23));
        assertEquals(single, Json.jObject(Map.of("k", Json.jNumber(23))));
        assertEquals(1, single.size());
        assertEquals(1, single.values().size());
        assertEquals(List.of(Json.jNumber(23)), single.values());
        assertTrue(Json.jEmptyObject().isEmpty());
        assertFalse(Json.jEmptyObject().containsKey("Hello"));
        assertTrue(single.containsKey("k"));
        assertTrue(single.containsValue(Json.jNumber(23)));
        assertEquals(single.mapToList(Json::tuple), List.of(Json.tuple("k", Json.jNumber(23))));
        assertEquals(single.put("v", Json.jEmptyArray()), Json.jObject(
                Json.tuple("k", Json.jNumber(23)),
                Json.tuple("v", Json.jEmptyArray())
        ));
    }

    @Test
    public void jObjectConcat() {
        Json.JObject single = Json.jObject("k", Json.jNumber(23));
        Json.JObject single2 = Json.jObject("k2", Json.jEmptyArray());
        Json.JObject concat = single.concat(single2);

        assertEquals(2, concat.size());
    }

    @Test
    public void nullTests() {
        testNullPointer(() -> Json.jNumber(null), "Number may not be null");
        testNullPointer(() -> Json.jString(null), "String may not be null");
        testNullPointer(() -> Json.jArray(null), "iterable was null");
        testNullPointer(() -> Json.jObject(null, 1), "Name for entry may not be null");
        testNullPointer(() -> Json.jObject("meh", (Json.JValue) null), "Value for named entry 'meh' may not be null");
    }

    @Test
    public void deepMerge() {
        Json.JValue merged = Json.jString("Hello").deepmerge(Json.jString("Bye"));
        assertEquals("Bye", merged.asString().orElse((String)null));

        Json.JObject entry = Json.jObject(
                Json.tuple("k1", Json.jString("k1")),
                Json.tuple("k2", Json.jString("k2")),
                Json.tuple("k3", false));

        assertEquals(entry, Json.jEmptyObject().deepmerge(entry));
        assertEquals(entry, entry.deepmerge(Json.jEmptyObject()));
        Json.JObject withObject = entry.put("k4", Json.jObject(
                "inner1", Json.jNumber(23)
        ));
        Json.JObject withObject2 = entry.put("k4", Json.jObject(
                "inner2", Json.jNumber(40)
        ));

        Json.JObject concat = withObject.concat(withObject2);
        Json.JObject withObject3 = entry.put("k4", Json.jObject(
                Json.tuple("inner1", Json.jNumber(23)),
                Json.tuple("inner2", Json.jNumber(40))
        ));
        assertTrue(concat.getAsObjectOrEmpty("k4").containsKey("inner2"));
        assertFalse(concat.getAsObjectOrEmpty("k4").containsKey("inner1"));
        assertEquals(withObject, entry.deepmerge(withObject));
        assertEquals(withObject2, entry.deepmerge(withObject2));
        assertEquals(withObject3, withObject.deepmerge(withObject2));
        assertEquals(withObject3, withObject2.deepmerge(withObject));
    }

    @Test
    public void serializable() throws IOException, ClassNotFoundException {
        Json.JObject object = Json.jObject(
                Json.tuple("foo", 1),
                Json.tuple("bar", "bar"),
                Json.tuple("meh", false),
                Json.tuple("mmm", Json.jNull())
        );
        assertSerializable(object);
        assertSerializable(Json.jString("Hello World"));
        assertSerializable(Json.jNumber(42));
        assertSerializable(Json.jNull());
        assertSerializable(Json.jArray(Json.jString("One"), Json.jNumber(2)));
        assertSerializable(Json.jBoolean(true));
    }

    @Test
    public void jObjectConstructors() {
        Json.JObject object = Json.jObject(
                "1", Json.jString("2")
        );

        assertObject(object, 1);

        object = Json.jObject(
                "1", Json.jNumber(1),
                "2", Json.jNumber(2)
        );

        assertObject(object, 2);


        object = Json.jObject(
                "1", Json.jNumber(1),
                "2", Json.jNumber(2),
                "3", Json.jNumber(3)
        );

        assertObject(object, 3);

        object = Json.jObject(
                "1", Json.jNumber(1),
                "2", Json.jNumber(2),
                "3", Json.jNumber(3),
                "4", Json.jNumber(4)
        );

        assertObject(object, 4);

        object = Json.jObject(
                "1", Json.jNumber(1),
                "2", Json.jNumber(2),
                "3", Json.jNumber(3),
                "4", Json.jNumber(4),
                "5", Json.jNumber(5)
        );


        assertObject(object, 5);

        object = Json.jObject(
                "1", Json.jNumber(1),
                "2", Json.jNumber(2),
                "3", Json.jNumber(3),
                "4", Json.jNumber(4),
                "5", Json.jNumber(5),
                "6", Json.jNumber(6)
        );

        assertObject(object, 6);

        object = Json.jObject(
                "1", Json.jNumber(1),
                "2", Json.jNumber(2),
                "3", Json.jNumber(3),
                "4", Json.jNumber(4),
                "5", Json.jNumber(5),
                "6", Json.jNumber(6),
                "7", Json.jNumber(7)
        );


        assertObject(object, 7);

        object = Json.jObject(
                "1", Json.jNumber(1),
                "2", Json.jNumber(2),
                "3", Json.jNumber(3),
                "4", Json.jNumber(4),
                "5", Json.jNumber(5),
                "6", Json.jNumber(6),
                "7", Json.jNumber(7),
                "8", Json.jNumber(8)
        );


        assertObject(object, 8);


        object = Json.jObject(
                "1", Json.jNumber(1),
                "2", Json.jNumber(2),
                "3", Json.jNumber(3),
                "4", Json.jNumber(4),
                "5", Json.jNumber(5),
                "6", Json.jNumber(6),
                "7", Json.jNumber(7),
                "8", Json.jNumber(8),
                "9", Json.jString("9")
        );

        assertObject(object, 9);


        object = Json.jObject(
                "1", Json.jNumber(1),
                "2", Json.jNumber(2),
                "3", Json.jNumber(3),
                "4", Json.jNumber(4),
                "5", Json.jNumber(5),
                "6", Json.jNumber(6),
                "7", Json.jNumber(7),
                "8", Json.jNumber(8),
                "9", Json.jString("9"),
                "10", Json.jBoolean(true)
        );
        assertObject(object, 10);
    }

    private void assertObject(Json.JObject object, int size) {
        assertEquals(object.size(), size);
        List<String> expectedKeys = jsonRange(1, size).stream().map(a -> a.asBigDecimal().get().toString()).collect(Collectors.toList());
        ArrayList<String> keys = new ArrayList<>(object.keySet());
        assertEquals(expectedKeys, keys);
    }

    private List<Json.JValue> jsonRange(int start, int end) {
        ArrayList<Json.JValue> list = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            list.add(Json.jNumber(i));
        }
        return List.copyOf(list);
    }

    private void testNullPointer(Runnable r, String message) {
        try {
            r.run();
            fail("Did not throw nullpointer");
        } catch (NullPointerException e) {
            assertEquals(message, e.getMessage());
        }
    }

    private void assertSerializable(Json.JValue input) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(bos);

        stream.writeObject(input);

        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        Json.JValue read = (Json.JValue) is.readObject();

        assertEquals(input, read);
    }
}
