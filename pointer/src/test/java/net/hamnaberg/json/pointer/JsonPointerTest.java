package net.hamnaberg.json.pointer;

import net.hamnaberg.json.Json;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class JsonPointerTest {
    private final Json.JObject json =
            Json.jObject( "collection",
                Json.jObject("links", Json.jArray(
                    Json.jObject("rel", Json.jString("feed"))
                    ))
    );

    @Test
    public void findRel() throws Exception {
        JsonPointer p = JsonPointer.compile("/collection/links/0/rel");
        Optional<Json.JValue> value = p.select(json);
        assertEquals(Optional.of(Json.jString("feed")), value);
    }
}
