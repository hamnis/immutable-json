package net.hamnaberg.json.codec;

import javaslang.collection.List;
import javaslang.control.Option;
import net.hamnaberg.json.Json;

import java.util.Optional;
import java.util.function.Function;

public abstract class Decoders {
    private Decoders(){}

    public static final DecodeJson<String> DString = value -> DecodeResult.fromOption(value.asString());
    public static final DecodeJson<Number> DNumber = value -> DecodeResult.fromOption(value.asBigDecimal().map(v -> (Number) v));
    public static final DecodeJson<Long> DLong = value -> DecodeResult.fromOption(value.asJsonNumber().map(Json.JNumber::asLong));
    public static final DecodeJson<Integer> DInt = value -> DecodeResult.fromOption(value.asJsonNumber().map(Json.JNumber::asInt));
    public static final DecodeJson<Double> DDouble = value -> DecodeResult.fromOption(value.asJsonNumber().map(Json.JNumber::asDouble));
    public static final DecodeJson<Boolean> DBoolean = value -> DecodeResult.fromOption(value.asBoolean());

    public static <A> DecodeJson<List<A>> listDecoder(DecodeJson<A> decoder) {
        return value -> DecodeResult.sequence(value.asJsonArrayOrEmpty().mapToList(decoder::fromJson));
    }

    public static <A> DecodeJson<java.util.List<A>> javaListDecoder(DecodeJson<A> codec) {
        return listDecoder(codec).map(List::toJavaList);
    }

    public static <A> DecodeJson<Option<A>> OptionDecoder(DecodeJson<A> codec) {
        DecodeJson<Option<A>> decoder = value -> value.isNull() ? DecodeResult.ok(Option.none()) : DecodeResult.ok(codec.fromJson(value).toOption());
        return decoder.withDefaultValue(Option.none());
    }

    public static <A> DecodeJson<Optional<A>> OptionalCodec(DecodeJson<A> underlying) {
        DecodeJson<Optional<A>> decoder = OptionDecoder(underlying).map(Option::toJavaOptional);
        return decoder.withDefaultValue(Optional.empty());
    }

    public static <A> DecodeJson<A> objectCodec(Function<Json.JObject, DecodeResult<A>> decoder) {
        return json -> decoder.apply(json.asJsonObjectOrEmpty());
    }
}
