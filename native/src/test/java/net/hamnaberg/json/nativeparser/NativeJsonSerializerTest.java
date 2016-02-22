package net.hamnaberg.json.nativeparser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.hamnaberg.json.Json;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class NativeJsonSerializerTest {
    @Test
    public void outputSameAsInput() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(getClass().getResourceAsStream("/items.json"));
        String expected = mapper.writeValueAsString(node);
        Json.JValue parse = new NativeJsonParser().parse(expected);
        String s = NativeJsonSerializer.nospaces().writeToString(parse);
        assertEquals(expected, s);
    }

    @Test
    public void canParseSerialized() throws Exception {
        Json.JValue parsed = new NativeJsonParser().parse(getClass().getResourceAsStream("/items.json"));
        String s = NativeJsonSerializer.nospaces().writeToString(parsed);
        Json.JValue parsedSerialized = new NativeJsonParser().parse(s);
        assertEquals(parsed, parsedSerialized);
        assertNotSame(parsed, parsedSerialized);
    }
}
