package net.hamnaberg.json;

import java.util.Optional;
import java.util.function.Function;

public interface EncodeJson<A> {
    Optional<Json.JValue> toJson(A value);

    default Json.JValue toJsonUnsafe(A value) {
        return toJson(value).orElse(null);
    }

    default <B> EncodeJson<B> contramap(Function<B, A> f) {
        return value -> this.toJson(f.apply(value));
    }
}
