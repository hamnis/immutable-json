package net.hamnaberg.json.io;

import net.hamnaberg.json.JsonValue;

@FunctionalInterface
public interface JsonSerializer<A> {
    A toJson(JsonValue value);
}
