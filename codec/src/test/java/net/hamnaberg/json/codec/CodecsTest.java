package net.hamnaberg.json.codec;

import net.hamnaberg.arities.*;
import net.hamnaberg.json.Json;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class CodecsTest {

    @Test
    public void collectionTests() {
        List<Integer> expectedList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3);
        Set<Integer> expectedSet = Set.copyOf(expectedList);
        Json.JArray jsonList = Json.jArray(expectedList.stream().map(Json::jNumber).collect(Collectors.toList()));

        assertEquals(expectedList, Codecs.listCodec(Codecs.CInt).fromJsonUnsafe(jsonList));
        assertEquals(jsonList, Codecs.listCodec(Codecs.CInt).toJson(expectedList));

        assertEquals(expectedSet, Codecs.setCodec(Codecs.CInt).fromJsonUnsafe(jsonList));
    }

    @Test
    public void collectionDefaultTest() {
        Json.JArray jsonList = Json.jEmptyArray();
        Json.JObject notJsonList = Json.jEmptyObject();

        assertEquals(List.of(), Codecs.listCodec(Codecs.CInt).fromJsonUnsafe(jsonList));
        assertEquals(List.of(), Codecs.listCodec(Codecs.CInt).fromJsonUnsafe(notJsonList));

        assertEquals(Set.of(), Codecs.setCodec(Codecs.CInt).fromJsonUnsafe(jsonList));
        assertEquals(Set.of(), Codecs.setCodec(Codecs.CInt).fromJsonUnsafe(notJsonList));
    }

    @Test
    public void codec3() throws Exception {
        JsonCodec<Tuple3<String, Integer, Integer>> codec = Codecs.codec(
                Iso.identity(),
                Codecs.CString.field("1"),
                Codecs.CInt.field("2"),
                Codecs.CInt.field("3")
        );

        Json.JObject expectedJson = Json.jObject(
                Json.tuple("1", Json.jString("Hello")),
                Json.tuple("2", Json.jNumber(22)),
                Json.tuple("3", Json.jNumber(11))
        );

        Tuple3<String, Integer, Integer> expectedTuple = Tuples.of("Hello", 22, 11);

        assertEquals(expectedTuple, codec.fromJsonUnsafe(expectedJson));
        assertEquals(expectedJson, codec.toJson(expectedTuple));
    }

    @Test
    public void codec4() throws Exception {
        JsonCodec<Tuple4<String, Integer, Integer, String>> codec = Codecs.codec(
                Iso.identity(),
                Codecs.CString.field("1"),
                Codecs.CInt.field("2"),
                Codecs.CInt.field("3"),
                Codecs.CString.field("4")
        );

        Json.JObject expectedJson = Json.jObject(
                Json.tuple("1", Json.jString("Hello")),
                Json.tuple("2", Json.jNumber(22)),
                Json.tuple("3", Json.jNumber(11)),
                Json.tuple("4", Json.jString("Goodbye"))
        );

        Tuple4<String, Integer, Integer, String> expectedTuple = Tuples.of("Hello", 22, 11, "Goodbye");

        assertEquals(expectedTuple, codec.fromJsonUnsafe(expectedJson));
        assertEquals(expectedJson, codec.toJson(expectedTuple));
    }

    @Test
    public void codec5() throws Exception {
        JsonCodec<Tuple5<String, Integer, Integer, String, Long>> codec = Codecs.codec(
                Iso.identity(),
                Codecs.CString.field("1"),
                Codecs.CInt.field("2"),
                Codecs.CInt.field("3"),
                Codecs.CString.field("4"),
                Codecs.CLong.field("5")
        );

        Json.JObject expectedJson = Json.jObject(
                Json.tuple("1", Json.jString("Hello")),
                Json.tuple("2", Json.jNumber(22)),
                Json.tuple("3", Json.jNumber(11)),
                Json.tuple("4", Json.jString("Goodbye")),
                Json.tuple("5", Json.jNumber(400000000L))
        );

        Tuple5<String, Integer, Integer, String, Long> expectedTuple = Tuples.of("Hello", 22, 11, "Goodbye", 400000000L);

        assertEquals(expectedTuple, codec.fromJsonUnsafe(expectedJson));
        assertEquals(expectedJson, codec.toJson(expectedTuple));
    }

    @Test
    public void codec6() throws Exception {
        JsonCodec<Tuple6<String, Integer, Integer, String, Long, Boolean>> codec = Codecs.codec(
                Iso.identity(),
                Codecs.CString.field("1"),
                Codecs.CInt.field("2"),
                Codecs.CInt.field("3"),
                Codecs.CString.field("4"),
                Codecs.CLong.field("5"),
                Codecs.CBoolean.field("6")
        );

        Json.JObject expectedJson = Json.jObject(
                Json.tuple("1", Json.jString("Hello")),
                Json.tuple("2", Json.jNumber(22)),
                Json.tuple("3", Json.jNumber(11)),
                Json.tuple("4", Json.jString("Goodbye")),
                Json.tuple("5", Json.jNumber(400000000L)),
                Json.tuple("6", Json.jBoolean(true))
        );

        Tuple6<String, Integer, Integer, String, Long, Boolean> expectedTuple =
                Tuples.of("Hello", 22, 11, "Goodbye", 400000000L, true);

        assertEquals(expectedTuple, codec.fromJsonUnsafe(expectedJson));
        assertEquals(expectedJson, codec.toJson(expectedTuple));
    }

    @Test
    public void codec7() throws Exception {
        JsonCodec<Tuple7<String, Integer, Integer, String, Long, Boolean, String>> codec = Codecs.codec(
                Iso.identity(),
                Codecs.CString.field("1"),
                Codecs.CInt.field("2"),
                Codecs.CInt.field("3"),
                Codecs.CString.field("4"),
                Codecs.CLong.field("5"),
                Codecs.CBoolean.field("6"),
                Codecs.CString.field("7")
        );

        Json.JObject expectedJson = Json.jObject(
                Json.tuple("1", Json.jString("Hello")),
                Json.tuple("2", Json.jNumber(22)),
                Json.tuple("3", Json.jNumber(11)),
                Json.tuple("4", Json.jString("Goodbye")),
                Json.tuple("5", Json.jNumber(400000000L)),
                Json.tuple("6", Json.jBoolean(true)),
                Json.tuple("7", Json.jString("Lucky no 7"))
        );

        Tuple7<String, Integer, Integer, String, Long, Boolean, String> expectedTuple =
                Tuples.of("Hello", 22, 11, "Goodbye", 400000000L, true, "Lucky no 7");

        assertEquals(expectedTuple, codec.fromJsonUnsafe(expectedJson));
        assertEquals(expectedJson, codec.toJson(expectedTuple));
    }

    @Test
    public void codec8() throws Exception {
        JsonCodec<Tuple8<String, Integer, Integer, String, Long, Boolean, String, Integer>> codec = Codecs.codec(
                Iso.identity(),
                Codecs.CString.field("1"),
                Codecs.CInt.field("2"),
                Codecs.CInt.field("3"),
                Codecs.CString.field("4"),
                Codecs.CLong.field("5"),
                Codecs.CBoolean.field("6"),
                Codecs.CString.field("7"),
                Codecs.CInt.field("8")
        );

        Json.JObject expectedJson = Json.jObject(
                Json.tuple("1", Json.jString("Hello")),
                Json.tuple("2", Json.jNumber(22)),
                Json.tuple("3", Json.jNumber(11)),
                Json.tuple("4", Json.jString("Goodbye")),
                Json.tuple("5", Json.jNumber(400000000L)),
                Json.tuple("6", Json.jBoolean(true)),
                Json.tuple("7", Json.jString("Lucky no 7")),
                Json.tuple("8", Json.jNumber(333333))
        );

        Tuple8<String, Integer, Integer, String, Long, Boolean, String, Integer> expectedTuple =
                Tuples.of("Hello", 22, 11, "Goodbye", 400000000L, true, "Lucky no 7", 333333);

        assertEquals(expectedTuple, codec.fromJsonUnsafe(expectedJson));
        assertEquals(expectedJson, codec.toJson(expectedTuple));
    }
}
