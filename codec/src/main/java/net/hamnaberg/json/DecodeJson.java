package net.hamnaberg.json;

import java.util.Optional;

public interface DecodeJson<A> {
    Optional<A> fromJson(Json.JValue value);
}
