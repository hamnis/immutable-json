package net.hamnaberg.json;

import java.util.Optional;

public interface EncodeJson<A> {
    Optional<Json.JValue> toJson(A value);

    default Json.JValue toJsonUnsafe(A value) {
        return toJson(value).orElse(null);
    }
}
