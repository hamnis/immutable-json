package net.hamnaberg.json.io;

import javaslang.control.Either;
import javaslang.control.Option;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.pointer.JsonPointer;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public abstract class JsonParserAbstractTest {
    @Test
    public void parseItemsJson() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/items.json");
        assertNotNull("Stream was null", stream);
        Either<Exception, Json.JValue> parsed = getParser().parse(stream);
        parsed.left().forEach(Throwable::printStackTrace);
        assertTrue(parsed.isRight());
        parsed.right().forEach(json -> {
            Option<Json.JValue> firstLink = json.
                    asJsonObjectOrEmpty().
                    getOrDefault("collection", Json.jEmptyObject()).
                    asJsonObjectOrEmpty().get("links").
                    flatMap(j2 -> j2.asJsonArrayOrEmpty().headOption());

            Option<Json.JValue> relValue = firstLink.flatMap(j -> j.asJsonObjectOrEmpty().get("rel"));
            assertTrue(relValue.isDefined());
            relValue.forEach(v -> assertEquals(Json.jString("feed"), v));
            Option<Json.JValue> value = JsonPointer.compile("/collection/links/0/rel").select(json);
            assertTrue(value.isDefined());
            value.forEach(v -> assertEquals(Json.jString("feed"), v));

            value = JsonPointer.compile("/collection/href").select(json);
            assertTrue(value.isDefined());
            value.forEach(v -> assertEquals(Json.jString("http://example.org/friends/"), v));
        });

    }

    protected abstract JsonParser getParser();
}
