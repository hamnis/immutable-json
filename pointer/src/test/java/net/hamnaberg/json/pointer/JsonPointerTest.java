package net.hamnaberg.json.pointer;

import javaslang.control.Option;
import net.hamnaberg.json.Json;
import org.junit.Test;

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
        Option<Json.JValue> value = p.select(json);
        assertEquals(Option.of(Json.jString("feed")), value);
    }
}
