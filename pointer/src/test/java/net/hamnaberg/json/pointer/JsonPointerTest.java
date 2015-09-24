package net.hamnaberg.json.pointer;

import net.hamnaberg.json.JsonArray;
import net.hamnaberg.json.JsonObject;
import net.hamnaberg.json.JsonString;
import net.hamnaberg.json.JsonValue;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class JsonPointerTest {
    private final JsonObject json =
            JsonObject.of( "collection",
                JsonObject.of("links", JsonArray.of(
                    JsonObject.of("rel", JsonString.of("feed"))
                    ))
    );

    @Test
    public void findRel() throws Exception {
        JsonPointer p = JsonPointer.compile("/collection/links/0/rel");
        Optional<JsonValue> value = p.select(json);
        assertEquals(Optional.of(JsonString.of("feed")), value);
    }
}
