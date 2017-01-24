package net.hamnaberg.json.codec;

import javaslang.collection.List;
import javaslang.control.Option;
import net.hamnaberg.json.Json;

import java.util.Optional;
import java.util.function.Function;

public abstract class Encoders {
    private Encoders(){}

    public static final EncodeJson<String> StringEncoder = value -> Option.some(Json.jString(value));
    public static final EncodeJson<Number> NumberEncoder = value -> Option.of(Json.jNumber(value));
    public static final EncodeJson<Long> LongEncoder = value -> Option.of(Json.jNumber(value));
    public static final EncodeJson<Integer> IntEncoder = value -> Option.of(Json.jNumber(value));
    public static final EncodeJson<Double> DoubleEncoder = value -> Option.of(Json.jNumber(value));
    public static final EncodeJson<Boolean> BooleanEncoder = value -> Option.of(Json.jBoolean(value));

    public static <A> EncodeJson<List<A>> listEncoder(EncodeJson<A> encoder) {
        return value -> Option.of(Json.jArray(value.flatMap(a -> encoder.toJson(a).toList())));
    }

    public static <A> EncodeJson<java.util.List<A>> javaListEncoder(EncodeJson<A> encoder) {
        return listEncoder(encoder).contramap(List::ofAll);
    }

    public static <A> EncodeJson<Option<A>> OptionEncoder(EncodeJson<A> encoder) {
        return value -> value.flatMap(encoder::toJson).orElse(Option.some(Json.jNull()));
    }

    public static <A> EncodeJson<Optional<A>> OptionalEncoder(EncodeJson<A> underlying) {
        return OptionEncoder(underlying).contramap(Option::ofOptional);
    }

    public static <A> EncodeJson<A> objectEncoder(Function<A, Json.JObject> encoder) {
        return a -> Option.some(encoder.apply(a).asJValue());
    }
}
