package net.hamnaberg.json.codec;

import net.hamnaberg.json.Json;

import java.util.function.Function;

public interface EncodeJson<A> {
    Json.JValue toJson(A value);

    @Deprecated
    default Json.JValue toJsonUnsafe(A value) {
        return toJson(value);
    }

    default <B> EncodeJson<B> contramap(Function<B, A> f) {
        return value -> this.toJson(f.apply(value));
    }

    default FieldEncoder<A> fieldEncoder(String name) {
        return FieldEncoder.typedFieldOf(name, this);
    }
}
