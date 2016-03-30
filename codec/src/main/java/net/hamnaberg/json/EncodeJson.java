package net.hamnaberg.json;

import javaslang.control.Option;

import java.util.function.Function;

public interface EncodeJson<A> {
    Option<Json.JValue> toJson(A value);

    default Json.JValue toJsonUnsafe(A value) {
        return toJson(value).getOrElse((Json.JValue)null);
    }

    default <B> EncodeJson<B> contramap(Function<B, A> f) {
        return value -> this.toJson(f.apply(value));
    }
}
