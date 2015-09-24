package net.hamnaberg.json.io;

import javaslang.control.Try;
import net.hamnaberg.json.JsonObject;
import net.hamnaberg.json.JsonString;
import net.hamnaberg.json.JsonValue;
import net.hamnaberg.json.pointer.JsonPointer;
import org.junit.Test;

import java.io.InputStream;
import java.util.Optional;

import static org.junit.Assert.*;

public abstract class JsonParserAbstractTest {
    @Test
    public void parseItemsJson() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/items.json");
        assertNotNull("Stream was null", stream);
        Try<JsonValue> parsed = getParser().parse(stream);
        parsed.onFailure(t -> {
            t.printStackTrace();
            throw t;
        });
        assertFalse(parsed.isFailure());
        parsed.forEach(json -> {
            Optional<JsonValue> firstLink = json.
                    asJsonObjectOrEmpty().
                    getOrDefault("collection", JsonObject.empty()).
                    asJsonObjectOrEmpty().get("links").
                    flatMap(j2 -> j2.asJsonArrayOrEmpty().get(0));

            Optional<JsonValue> relValue = firstLink.flatMap(j -> j.asJsonObjectOrEmpty().get("rel"));
            assertTrue(relValue.isPresent());
            relValue.ifPresent(v -> assertEquals(new JsonString("feed"), v));
            Optional<JsonValue> value = JsonPointer.compile("/collection/links/0/rel").select(json);
            assertTrue(value.isPresent());
            value.ifPresent(v -> assertEquals(new JsonString("feed"), v));

            value = JsonPointer.compile("/collection/href").select(json);
            assertTrue(value.isPresent());
            value.ifPresent(v -> assertEquals(new JsonString("http://example.org/friends/"), v));
        });

    }

    protected abstract JsonParser getParser();
}
