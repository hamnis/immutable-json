package net.hamnaberg.json.codec;

import io.vavr.*;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.util.*;

import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public abstract class Decoders {
    private Decoders(){}

    public static final DecodeJson<Json.JValue> DIdentity = DecodeResult::ok;
    public static final DecodeJson<String> DString = value -> DecodeResult.fromOption(value.asString());
    public static final DecodeJson<Number> DNumber = value -> DecodeResult.fromOption(value.asBigDecimal().map(v -> (Number) v));
    public static final DecodeJson<Long> DLong = value -> DecodeResult.fromOption(value.asJsonNumber().map(Json.JNumber::asLong));
    public static final DecodeJson<Integer> DInt = value -> DecodeResult.fromOption(value.asJsonNumber().map(Json.JNumber::asInt));
    public static final DecodeJson<Double> DDouble = value -> DecodeResult.fromOption(value.asJsonNumber().map(Json.JNumber::asDouble));
    public static final DecodeJson<Boolean> DBoolean = value -> DecodeResult.fromOption(value.asBoolean());
    public static final DecodeJson<UUID> DUUID = DString.tryMap(s -> Try.of(() -> UUID.fromString(s)));
    public static final DecodeJson<URI> DURI = DString.tryMap(s -> Try.of(() -> URI.create(s)));
    public static final DecodeJson<URL> DURL = DURI.tryMap(uri -> Try.of(uri::toURL));
    public static final DecodeJson<ZonedDateTime> DISODateTimeUTC = zonedDateTimeDecoder(DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC));
    public static final DecodeJson<Instant> DISOInstantUTC = instantDecoder(DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC));

    public static DecodeJson<ZonedDateTime> zonedDateTimeDecoder(DateTimeFormatter formatter) {
        return DString.tryMap(s -> Try.of(() -> ZonedDateTime.parse(s, formatter)));
    }

    public static DecodeJson<Instant> instantDecoder(DateTimeFormatter formatter) {
        return DString.tryMap(s -> Try.of(() -> formatter.parse(s, Instant::from)));
    }

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

    @Deprecated
    /**
     *
     * @deprecated use {@link #optionalDecoder(DecodeJson)} instead
     */
    public static <A> DecodeJson<Optional<A>> OptionalCodec(DecodeJson<A> underlying) {
        return optionalDecoder(underlying);
    }

    @Deprecated
    /**
     *
     * @deprecated use {@link #objectDecoder} instead
     */
    public static <A> DecodeJson<A> objectCodec(Function<Json.JObject, DecodeResult<A>> decoder) {
        return json -> decoder.apply(json.asJsonObjectOrEmpty());
    }

    public static <A> DecodeJson<Optional<A>> optionalDecoder(DecodeJson<A> underlying) {
        DecodeJson<Optional<A>> decoder = OptionDecoder(underlying).map(Option::toJavaOptional);
        return decoder.withDefaultValue(Optional.empty());
    }

    public static <A> DecodeJson<A> objectDecoder(Function<Json.JObject, DecodeResult<A>> decoder) {
        return json -> decoder.apply(json.asJsonObjectOrEmpty());
    }


    public static <TT, A> DecodeJson<TT> decode(FieldDecoder<A> f1, Function1<A, TT> func) {
        return (value) -> {
            DecodeResult<A> oa = DecodeResult.decode(value.asJsonObjectOrEmpty(), f1.name, f1.decoder);
            return oa.flatMap(a -> DecodeResult.ok(func.apply(a)));
        };
    }

    public static <TT, A1, A2> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, Function2<A1, A2, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 ->  DecodeResult.ok(func.apply(v1, v2))));
        };
    }

    public static <TT, A1, A2, A3> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, Function3<A1, A2, A3, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 ->  DecodeResult.ok(func.apply(v1, v2, v3)))));
        };
    }

    public static <TT, A1, A2, A3, A4> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, Function4<A1, A2, A3, A4, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, Function5<A1, A2, A3, A4, A5, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5)))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, Function6<A1, A2, A3, A4, A5, A6, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, Function7<A1, A2, A3, A4, A5, A6, A7, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7)))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, Function8<A1, A2, A3, A4, A5, A6, A7, A8, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, F9<A1, A2, A3, A4, A5, A6, A7, A8, A9, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9)))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, F10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, F11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, F12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, F13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, F14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, F15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, F16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, fd16.name, fd16.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, F17<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, fd16.name, fd16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, fd17.name, fd17.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, F18<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, fd16.name, fd16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, fd17.name, fd17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, fd18.name, fd18.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, F19<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, fd16.name, fd16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, fd17.name, fd17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, fd18.name, fd18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, fd19.name, fd19.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, F20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, fd16.name, fd16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, fd17.name, fd17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, fd18.name, fd18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, fd19.name, fd19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, fd20.name, fd20.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, F21<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, fd16.name, fd16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, fd17.name, fd17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, fd18.name, fd18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, fd19.name, fd19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, fd20.name, fd20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, fd21.name, fd21.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, FieldDecoder<A22> fd22, F22<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, fd16.name, fd16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, fd17.name, fd17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, fd18.name, fd18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, fd19.name, fd19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, fd20.name, fd20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, fd21.name, fd21.decoder);
            DecodeResult<A22> d22 = DecodeResult.decode(object, fd22.name, fd22.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, FieldDecoder<A22> fd22, FieldDecoder<A23> fd23, F23<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, fd16.name, fd16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, fd17.name, fd17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, fd18.name, fd18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, fd19.name, fd19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, fd20.name, fd20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, fd21.name, fd21.decoder);
            DecodeResult<A22> d22 = DecodeResult.decode(object, fd22.name, fd22.decoder);
            DecodeResult<A23> d23 = DecodeResult.decode(object, fd23.name, fd23.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23)))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, FieldDecoder<A22> fd22, FieldDecoder<A23> fd23, FieldDecoder<A24> fd24, F24<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, fd16.name, fd16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, fd17.name, fd17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, fd18.name, fd18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, fd19.name, fd19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, fd20.name, fd20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, fd21.name, fd21.decoder);
            DecodeResult<A22> d22 = DecodeResult.decode(object, fd22.name, fd22.decoder);
            DecodeResult<A23> d23 = DecodeResult.decode(object, fd23.name, fd23.decoder);
            DecodeResult<A24> d24 = DecodeResult.decode(object, fd24.name, fd24.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24))))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, FieldDecoder<A22> fd22, FieldDecoder<A23> fd23, FieldDecoder<A24> fd24, FieldDecoder<A25> fd25, F25<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, fd16.name, fd16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, fd17.name, fd17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, fd18.name, fd18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, fd19.name, fd19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, fd20.name, fd20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, fd21.name, fd21.decoder);
            DecodeResult<A22> d22 = DecodeResult.decode(object, fd22.name, fd22.decoder);
            DecodeResult<A23> d23 = DecodeResult.decode(object, fd23.name, fd23.decoder);
            DecodeResult<A24> d24 = DecodeResult.decode(object, fd24.name, fd24.decoder);
            DecodeResult<A25> d25 = DecodeResult.decode(object, fd25.name, fd25.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 -> d25.flatMap(v25 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25)))))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, FieldDecoder<A22> fd22, FieldDecoder<A23> fd23, FieldDecoder<A24> fd24, FieldDecoder<A25> fd25, FieldDecoder<A26> fd26, F26<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, fd16.name, fd16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, fd17.name, fd17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, fd18.name, fd18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, fd19.name, fd19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, fd20.name, fd20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, fd21.name, fd21.decoder);
            DecodeResult<A22> d22 = DecodeResult.decode(object, fd22.name, fd22.decoder);
            DecodeResult<A23> d23 = DecodeResult.decode(object, fd23.name, fd23.decoder);
            DecodeResult<A24> d24 = DecodeResult.decode(object, fd24.name, fd24.decoder);
            DecodeResult<A25> d25 = DecodeResult.decode(object, fd25.name, fd25.decoder);
            DecodeResult<A26> d26 = DecodeResult.decode(object, fd26.name, fd26.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 -> d25.flatMap(v25 -> d26.flatMap(v26 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26))))))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, FieldDecoder<A22> fd22, FieldDecoder<A23> fd23, FieldDecoder<A24> fd24, FieldDecoder<A25> fd25, FieldDecoder<A26> fd26, FieldDecoder<A27> fd27, F27<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27, TT> func) {
        return (value) -> {
            Json.JObject object = value.asJsonObjectOrEmpty();
            DecodeResult<A1> d1 = DecodeResult.decode(object, fd1.name, fd1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, fd2.name, fd2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, fd3.name, fd3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, fd4.name, fd4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, fd5.name, fd5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, fd6.name, fd6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, fd7.name, fd7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, fd8.name, fd8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, fd9.name, fd9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, fd10.name, fd10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, fd11.name, fd11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, fd12.name, fd12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, fd13.name, fd13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, fd14.name, fd14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, fd15.name, fd15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, fd16.name, fd16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, fd17.name, fd17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, fd18.name, fd18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, fd19.name, fd19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, fd20.name, fd20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, fd21.name, fd21.decoder);
            DecodeResult<A22> d22 = DecodeResult.decode(object, fd22.name, fd22.decoder);
            DecodeResult<A23> d23 = DecodeResult.decode(object, fd23.name, fd23.decoder);
            DecodeResult<A24> d24 = DecodeResult.decode(object, fd24.name, fd24.decoder);
            DecodeResult<A25> d25 = DecodeResult.decode(object, fd25.name, fd25.decoder);
            DecodeResult<A26> d26 = DecodeResult.decode(object, fd26.name, fd26.decoder);
            DecodeResult<A27> d27 = DecodeResult.decode(object, fd27.name, fd27.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 -> d25.flatMap(v25 -> d26.flatMap(v26 -> d27.flatMap(v27 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27)))))))))))))))))))))))))))));
        };
    }
}
