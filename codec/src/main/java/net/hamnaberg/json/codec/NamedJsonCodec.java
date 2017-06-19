package net.hamnaberg.json.codec;

import io.vavr.control.Option;
import net.hamnaberg.json.Json;

public class NamedJsonCodec<A> implements JsonCodec<A> {
    public final String name;
    final JsonCodec<A> codec;

    private NamedJsonCodec(String name, JsonCodec<A> codec) {
        this.name = name;
        this.codec = codec;
    }

    public static <A> NamedJsonCodec<A> of(String name, JsonCodec<A> codec) {
        if (codec instanceof NamedJsonCodec) {
            return new NamedJsonCodec<>(name, ((NamedJsonCodec<A>) codec).codec);
        }
        return new NamedJsonCodec<>(name, codec);
    }

    public FieldDecoder<A> toFieldDecoder() {
        return FieldDecoder.typedFieldOf(name, codec);
    }

    public FieldEncoder<A> toFieldEncoder() {
        return FieldEncoder.typedFieldOf(name, codec);
    }

    @Override
    public DecodeResult<A> fromJson(Json.JValue value) {
        return codec.fromJson(value);
    }

    @Override
    public Json.JValue toJson(A value) {
        return codec.toJson(value);
    }

    @Override
    public Option<A> defaultValue() {
        return codec.defaultValue();
    }

    @Override
    public String toString() {
        return "NamedJsonCodec{" +
                "name='" + name + '\'' +
                ", codec=" + codec +
                '}';
    }
}
