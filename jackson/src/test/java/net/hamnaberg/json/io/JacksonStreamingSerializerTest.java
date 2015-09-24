package net.hamnaberg.json.io;

import javaslang.control.Try;
import net.hamnaberg.json.JsonValue;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class JacksonStreamingSerializerTest {
    @Test
    public void outputSameAsInput() throws Exception {
        Try<JsonValue> parse = new JacksonStreamingParser().parse(getClass().getResourceAsStream("/items.json"));
        parse.forEach(jv -> {
            Consumer<OutputStream> consumer = new JacksonStreamingSerializer().toJson(jv);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            consumer.accept(bs);
            byte[] bytes = bs.toByteArray();
            String s = new String(bytes, StandardCharsets.UTF_8);
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
