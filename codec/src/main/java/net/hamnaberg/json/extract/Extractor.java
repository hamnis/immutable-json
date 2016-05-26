package net.hamnaberg.json.extract;

import net.hamnaberg.json.DecodeJson;
import net.hamnaberg.json.DecodeResult;
import net.hamnaberg.json.Json;

import java.util.function.Function;

@FunctionalInterface
public interface Extractor<A> extends Function<Json.JObject, DecodeResult<A>> {

    default DecodeJson<A> decoder() {
        return (json) -> this.apply(json.asJsonObjectOrEmpty());
    }
}
