package net.hamnaberg.json.codec;

import javaslang.control.Try;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.codec.Codecs;
import net.hamnaberg.json.codec.DecodeResult;
import net.hamnaberg.json.codec.JsonCodec;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertTrue;

public class BasicCodecTest {
    @Test
    public void narrowAndTryNarrow() {
        JsonCodec<URI> uriCodec = Codecs.StringCodec.narrow(s -> Try.of(() -> URI.create(s)), URI::toString);
        JsonCodec<URI> uriCodec2 = Codecs.StringCodec.tryNarrow(URI::create, URI::toString);

        DecodeResult<URI> invalid = uriCodec.fromJson(Json.jString("BA90lkldsf{}"));
        DecodeResult<URI> invalid2 = uriCodec2.fromJson(Json.jString("BA90lkldsf{}"));
        DecodeResult<URI> success = uriCodec2.fromJson(Json.jString("balle"));
        assertTrue(invalid.isFailure());
        assertTrue(invalid2.isFailure());
        assertTrue(success.isOk());
    }

}
