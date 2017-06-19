package net.hamnaberg.json.pointer;

import io.vavr.control.Option;
import net.hamnaberg.json.Json;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

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

    @Test
    public void validateToString() throws Exception {
        String pattern = "/collection/links/0/rel";
        JsonPointer p = JsonPointer.compile(pattern);
        assertEquals(pattern, p.toString());
        assertEquals("/", JsonPointer.compile("/").toString());
        assertEquals("", JsonPointer.compile("").toString());
    }

    @Test
    public void validateEscapedValuesToString() throws Exception {
        assertEquals("/a~1b", JsonPointer.compile("/a~1b").toString());
        assertEquals("/m~0n", JsonPointer.compile("/m~0n").toString());
    }

    @Test
    public void addHref() throws Exception {
        JsonPointer p = JsonPointer.compile("/collection/links/0/href");
        Json.JValue value = p.add(json, Json.jString("http://example.com"));
        Json.JObject modified =
                Json.jObject( "collection",
                        Json.jObject("links", Json.jArray(
                                Json.jObject(
                                        Json.tuple("rel", Json.jString("feed")),
                                        Json.tuple("href", Json.jString("http://example.com"))
                                )
                        ))
                );

        assertNotSame(json, value);
        assertEquals(modified, value);
    }

    @Test
    public void removeRel() throws Exception {
        JsonPointer p = JsonPointer.compile("/collection/links/0/rel");
        Json.JValue value = p.remove(json);
        Json.JObject modified =
                Json.jObject( "collection",
                        Json.jObject("links", Json.jArray(
                                Json.jEmptyObject()
                        ))
                );

        assertNotSame(json, value);
        assertEquals(modified, value);
    }

    @Test
    public void replaceRel() throws Exception {
        JsonPointer p = JsonPointer.compile("/collection/links/0/rel");
        Json.JValue value = p.replace(json, Json.jString("meh"));
        Json.JObject modified =
                Json.jObject( "collection",
                        Json.jObject("links", Json.jArray(
                                Json.jObject(
                                        Json.tuple("rel", Json.jString("meh"))
                                )
                        ))
                );

        assertNotSame(json, value);
        assertEquals(modified, value);
    }
}
