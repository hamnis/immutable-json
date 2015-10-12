package net.hamnaberg.json.io;

import net.hamnaberg.json.Json;
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
        Json.JValue parsed = getParser().parse(stream);
        Optional<Json.JValue> firstLink = parsed.
                asJsonObjectOrEmpty().
                getOrDefault("collection", Json.jEmptyObject()).
                asJsonObjectOrEmpty().get("links").
                flatMap(j2 -> j2.asJsonArrayOrEmpty().headOption());

        Optional<Json.JValue> relValue = firstLink.flatMap(j -> j.asJsonObjectOrEmpty().get("rel"));
        assertTrue(relValue.isPresent());
        relValue.ifPresent(v -> assertEquals(Json.jString("feed"), v));
        Optional<Json.JValue> value = JsonPointer.compile("/collection/links/0/rel").select(parsed);
        assertTrue(value.isPresent());
        value.ifPresent(v -> assertEquals(Json.jString("feed"), v));

        value = JsonPointer.compile("/collection/href").select(parsed);
        assertTrue(value.isPresent());
        assertEquals(Json.jString("http://example.org/friends/"), value.get());
    }

    protected abstract JsonParser getParser();
}
