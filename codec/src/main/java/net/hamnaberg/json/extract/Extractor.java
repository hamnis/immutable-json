package net.hamnaberg.json.extract;

import net.hamnaberg.json.Json;

import java.util.Optional;
import java.util.function.Function;

public interface Extractor<A> extends Function<Json.JObject, Optional<A>> {
}
