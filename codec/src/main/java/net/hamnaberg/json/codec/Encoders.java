package net.hamnaberg.json.codec;

import javaslang.collection.List;
import javaslang.control.Option;
import net.hamnaberg.json.Json;

import java.util.Optional;
import java.util.function.Function;

public abstract class Encoders {
    private Encoders(){}

    public static final EncodeJson<String> EString = Json::jString;
    public static final EncodeJson<Number> ENumber = Json::jNumber;
    public static final EncodeJson<Long> ELong = Json::jNumber;
    public static final EncodeJson<Integer> EInt = Json::jNumber;
    public static final EncodeJson<Double> EDouble = Json::jNumber;
    public static final EncodeJson<Boolean> EBoolean = Json::jBoolean;

    public static <A> EncodeJson<List<A>> listEncoder(EncodeJson<A> encoder) {
        return value -> Json.jArray(value.map(encoder::toJson));
    }

    public static <A> EncodeJson<java.util.List<A>> javaListEncoder(EncodeJson<A> encoder) {
        return listEncoder(encoder).contramap(List::ofAll);
    }

    public static <A> EncodeJson<Option<A>> OptionEncoder(EncodeJson<A> encoder) {
        return value -> value.map(encoder::toJson).getOrElse(Json.jNull());
    }

    public static <A> EncodeJson<Optional<A>> OptionalEncoder(EncodeJson<A> underlying) {
        return OptionEncoder(underlying).contramap(Option::ofOptional);
    }

    public static <A> EncodeJson<A> objectEncoder(Function<A, Json.JObject> encoder) {
        return a -> encoder.apply(a).asJValue();
    }
}
