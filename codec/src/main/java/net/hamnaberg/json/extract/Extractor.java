package net.hamnaberg.json.extract;

import net.hamnaberg.json.codec.DecodeJson;
import net.hamnaberg.json.codec.DecodeResult;
import net.hamnaberg.json.Json;

import java.util.function.Function;

@FunctionalInterface
@Deprecated
/**
 * @deprecated Use {@link DecodeJson} with {@link net.hamnaberg.json.codec.FieldDecoder}s instead.
 */
public interface Extractor<A> extends Function<Json.JObject, DecodeResult<A>> {

    default DecodeJson<A> decoder() {
        return (json) -> this.apply(json.asJsonObjectOrEmpty());
    }
}
