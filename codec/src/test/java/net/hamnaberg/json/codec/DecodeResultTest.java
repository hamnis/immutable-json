package net.hamnaberg.json.codec;

import net.hamnaberg.json.Json;
import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DecodeResultTest {

    @Test
    public void sequencing() throws Exception {
        DecodeResult<String> res1 = DecodeResult.ok("Hello");
        DecodeResult<String> res2 = DecodeResult.ok("World");
        DecodeResult<List<String>> sequenced = DecodeResult.sequence(List.of(res1, res2));
        assertEquals(List.of("Hello", "World"), sequenced.unsafeGet());
    }


    @Test
    public void sequencingFail() throws Exception {
        DecodeResult<String> res1 = DecodeResult.fail("Unable to decode [this]");
        DecodeResult<String> res2 = DecodeResult.ok("Hello");
        DecodeResult<List<String>> sequenced = DecodeResult.sequence(List.of(res1, res2));
        sequenced.consume(f -> assertEquals("One or more results failed: Unable to decode [this]", f), err -> fail("success where expected fail"));
    }

    @Test
    public void decodeFromObjectMissing() throws Exception {
        DecodeResult<String> name = DecodeResult.decode(Json.jEmptyObject(), "name", Decoders.DString);
        name.consume(f -> assertEquals("'name' not found in {}", f), err -> fail("success where expected fail"));
    }

    @Test
    public void decodeFromObjectFail() throws Exception {
        DecodeResult<String> name = DecodeResult.decode(Json.jObject("name", 1), "name", Decoders.DString);
        name.consume(f -> assertEquals("'name' failed with message: 'JNumber is not a JString'", f), err -> fail("success where expected fail"));
    }


    @Test
    public void filter() throws Exception {
        DecodeResult<String> res2 = DecodeResult.ok("Hello");
        assertEquals("Hello", res2.filter(a -> a.equals("Hello")).unsafeGet());
        assertEquals("Hello", res2.filter(Predicate.isEqual("Hello")).unsafeGet());
        assertEquals("Hello", res2.filter(Objects::nonNull).unsafeGet());
    }

    @Test(expected = NoSuchElementException.class)
    public void filterFail() throws Exception {
        DecodeResult<String> res2 = DecodeResult.ok("Hello");
        res2.filter("Goodbye"::equals).unsafeGet();
    }

    @Test
    public void filterFailCustomError() throws Exception {
        DecodeResult<String> res2 = DecodeResult.ok("Hello");
        res2.filter("Goodbye"::equals, () -> "Nope").consume(a -> assertEquals("Nope", a), a -> fail("Succeeded when expected fail"));
    }

    /*@Test
    public void toEither() throws Exception {
        DecodeResult<String> res2 = DecodeResult.ok("Hello");
        assertEquals("Hello", res2.toEither().get());
    }*/

    @Test
    public void toJavaOptional() throws Exception {
        DecodeResult<String> res2 = DecodeResult.ok("Hello");
        assertEquals("Hello", res2.toOption().get());
    }
}
