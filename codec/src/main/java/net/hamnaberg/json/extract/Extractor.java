package net.hamnaberg.json.extract;

import javaslang.control.Option;
import net.hamnaberg.json.Json;

import java.util.function.Function;

@FunctionalInterface
public interface Extractor<A> extends Function<Json.JObject, Option<A>> {
}
