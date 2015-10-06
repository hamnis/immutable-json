package net.hamnaberg.json.io;

import javaslang.control.Either;
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
        Either<Exception, Json.JValue> parse = new JacksonStreamingParser().parse(getClass().getResourceAsStream("/items.json"));
        parse.right().forEach(jv -> {
            String s = new JacksonStreamingSerializer().writeToString(jv);
            String expected = toString(getClass().getResourceAsStream("/items.json"));
            assertEquals(expected.replace(": ", ":"), s);
        });
    }

    String toString(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().map(String::trim).collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
