package net.hamnaberg.json.codec;

import io.vavr.collection.List;
import org.junit.Test;

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
}
