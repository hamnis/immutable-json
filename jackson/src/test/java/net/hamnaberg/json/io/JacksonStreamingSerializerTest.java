package net.hamnaberg.json.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.hamnaberg.json.Json;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class JacksonStreamingSerializerTest {
    @Test
    public void outputSameAsInput() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(getClass().getResourceAsStream("/items.json"));
        String expected = mapper.writeValueAsString(node);

        Json.JValue parse = new JacksonStreamingParser().parse(getClass().getResourceAsStream("/items.json"));
        String s = new JacksonStreamingSerializer().writeToString(parse);
        assertEquals(expected, s);
    }
}
