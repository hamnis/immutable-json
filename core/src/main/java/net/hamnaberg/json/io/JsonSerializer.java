package net.hamnaberg.json.io;

import net.hamnaberg.json.Json;

@FunctionalInterface
public interface JsonSerializer<A> {
    A toJson(Json.JValue value);
}
