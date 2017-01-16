package net.hamnaberg.json.io;


import net.hamnaberg.json.Json;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class JsonSerializerTest {

    @Test
    public void writer() throws IOException {
        Json.JObject object = Json.jObject("meh", "hello");

        try (StringWriter s = new StringWriter()) {
            JsonSerializer.write(object, s);
            String expected = "{\"meh\":\"hello\"}";
            assertEquals(expected, s.toString());
        }

    }

    @Test
    public void stream() throws IOException {
        Json.JObject object = Json.jObject("meh", "hello");

        try (ByteArrayOutputStream s = new ByteArrayOutputStream()) {
            JsonSerializer.write(object, s);
            String expected = "{\"meh\":\"hello\"}";
            assertEquals(expected, s.toString("UTF-8"));
        }
    }

    @Test
    public void string() throws IOException {
        Json.JObject object = Json.jObject("meh", "hello");
        String expected = "{\"meh\":\"hello\"}";

        assertEquals(expected, JsonSerializer.writeToString(object));
    }
}
