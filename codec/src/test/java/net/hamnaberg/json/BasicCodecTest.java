package net.hamnaberg.json;

import javaslang.control.Try;
import org.junit.Test;

import java.net.URI;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BasicCodecTest {
    @Test
    public void narrowAndTryNarrow() {

        JsonCodec<URI> uriCodec = Codecs.StringCodec.narrow(s -> Try.of(() -> URI.create(s)), URI::toString);
        JsonCodec<URI> uriCodec2 = Codecs.StringCodec.tryNarrow(URI::create, URI::toString);

        Optional<URI> invalid = uriCodec.fromJson(Json.jString("BA90lkldsf{}"));
        Optional<URI> invalid2 = uriCodec2.fromJson(Json.jString("BA90lkldsf{}"));
        Optional<URI> success = uriCodec2.fromJson(Json.jString("balle"));
        assertFalse(invalid.isPresent());
        assertFalse(invalid2.isPresent());
        assertTrue(success.isPresent());
    }

}
