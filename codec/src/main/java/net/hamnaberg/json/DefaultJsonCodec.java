package net.hamnaberg.json;

import javaslang.control.Option;

/**
 * Created by maedhros on 25/08/16.
 */
class DefaultJsonCodec<A> implements JsonCodec<A> {
    private DecodeJson<A> decoder;
    private EncodeJson<A> encoder;

    DefaultJsonCodec(DecodeJson<A> decoder, EncodeJson<A> encoder) {
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    public DecodeResult<A> fromJson(Json.JValue value) {
        return decoder.fromJson(value);
    }

    @Override
    public Option<Json.JValue> toJson(A value) {
        return encoder.toJson(value);
    }

    @Override
    public Option<A> defaultValue() {
        return decoder.defaultValue();
    }

    public String toString() {
        return String.format("DefaultCodec(decoder=%s, encoder=%s)", decoder, encoder);
    }
}
