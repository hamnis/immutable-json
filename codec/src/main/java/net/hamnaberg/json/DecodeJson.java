package net.hamnaberg.json;

import java.util.function.Function;

public interface DecodeJson<A> {
    DecodeResult<A> fromJson(Json.JValue value);

    default A fromJsonUnsafe(Json.JValue value) {
        return fromJson(value).toOption().getOrElse((A)null);
    }

    default <B> DecodeJson<B> map(Function<A, B> f) {
        return (json) -> this.fromJson(json).map(f);
    }

    default <B> DecodeJson<B> flatMap(Function<A, DecodeJson<B>> f) {
        return value -> {
            DecodeResult<A> result = this.fromJson(value);
            return result.flatMap(a -> f.apply(a).fromJson(value));
        };
    }
}
