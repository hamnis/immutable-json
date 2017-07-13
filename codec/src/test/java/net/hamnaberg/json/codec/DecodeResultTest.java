package net.hamnaberg.json.codec;

import io.vavr.Predicates;
import io.vavr.collection.List;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
    public void filter() throws Exception {
        DecodeResult<String> res2 = DecodeResult.ok("Hello");
        assertEquals("Hello", res2.filter(a -> a.equals("Hello")).unsafeGet());
        assertEquals("Hello", res2.filter(Predicate.isEqual("Hello")).unsafeGet());
        assertEquals("Hello", res2.filter(Predicates.isNotNull()).unsafeGet());
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

    @Test
    public void toEither() throws Exception {
        DecodeResult<String> res2 = DecodeResult.ok("Hello");
        assertEquals("Hello", res2.toEither().get());
    }

    @Test
    public void toJavaOptional() throws Exception {
        DecodeResult<String> res2 = DecodeResult.ok("Hello");
        assertEquals("Hello", res2.toJavaOptional().get());
    }
}
