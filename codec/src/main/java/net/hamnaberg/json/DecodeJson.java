package net.hamnaberg.json;

import java.util.Optional;
import java.util.function.Function;

public interface DecodeJson<A> {
    Optional<A> fromJson(Json.JValue value);

    default A fromJsonUnsafe(Json.JValue value) {
        return fromJson(value).orElse(null);
    }

    default <B> DecodeJson<B> map(Function<A, B> f) {
        return (json) -> this.fromJson(json).map(f);
    }

    default <B> DecodeJson<B> flatMap(Function<A, DecodeJson<B>> f) {
        return value -> {
            Optional<A> opt = this.fromJson(value);
            return opt.flatMap(a -> f.apply(a).fromJson(value));
        };
    }
}
