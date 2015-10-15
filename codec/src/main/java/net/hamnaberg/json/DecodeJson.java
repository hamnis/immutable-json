package net.hamnaberg.json;

import java.util.Optional;

public interface DecodeJson<A> {
    Optional<A> fromJson(Json.JValue value);

    default A fromJsonUnsafe(Json.JValue value) {
        return fromJson(value).orElse(null);
    }
}
