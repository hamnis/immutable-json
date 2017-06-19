package net.hamnaberg.json.codec;

import io.vavr.control.Option;
import net.hamnaberg.json.Json;

final class DefaultJsonCodec<A> implements JsonCodec<A> {
    private final String toString;
    private final DecodeJson<A> decoder;
    private final EncodeJson<A> encoder;

    DefaultJsonCodec(DecodeJson<A> decoder, EncodeJson<A> encoder) {
        this(decoder, encoder, String.format("DefaultCodec(decoder=%s, encoder=%s)", decoder, encoder));
    }

    DefaultJsonCodec(DecodeJson<A> decoder, EncodeJson<A> encoder, String toString) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.toString = toString;
    }

    @Override
    public DecodeResult<A> fromJson(Json.JValue value) {
        return decoder.fromJson(value);
    }

    @Override
    public Json.JValue toJson(A value) {
        return encoder.toJson(value);
    }

    @Override
    public Option<A> defaultValue() {
        return decoder.defaultValue();
    }

    public String toString() {
        return toString;
    }
}
