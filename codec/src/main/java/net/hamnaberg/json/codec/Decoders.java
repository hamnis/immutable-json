package net.hamnaberg.json.codec;

import net.hamnaberg.arities.*;
import net.hamnaberg.json.Json;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Decoders {
    private Decoders(){}

    public static final DecodeJson<Json.JValue> DIdentity = DecodeResult::ok;
    public static final DecodeJson<String> DString = value -> DecodeResult.fromOption(value.asString(), () -> String.format("%s is not a JString", value.getClass().getSimpleName()));
    public static final DecodeJson<BigDecimal> DBigDecimal = value -> DecodeResult.fromOption(value.asBigDecimal(), () -> String.format("%s is not a JNumber", value.getClass().getSimpleName()));
    public static final DecodeJson<Number> DNumber = DBigDecimal.map(bd -> (Number) bd);
    public static final DecodeJson<Long> DLong = value -> DecodeResult.fromOption(value.asJsonNumber().map(Json.JNumber::asLong), () -> String.format("%s is not a JNumber", value.getClass().getSimpleName()));
    public static final DecodeJson<Integer> DInt = value -> DecodeResult.fromOption(value.asJsonNumber().map(Json.JNumber::asInt), () -> String.format("%s is not a JNumber", value.getClass().getSimpleName()));
    public static final DecodeJson<Double> DDouble = value -> DecodeResult.fromOption(value.asJsonNumber().map(Json.JNumber::asDouble), () -> String.format("%s is not a JNumber", value.getClass().getSimpleName()));
    public static final DecodeJson<Boolean> DBoolean = value -> DecodeResult.fromOption(value.asBoolean(), () -> String.format("%s is not a JBoolean", value.getClass().getSimpleName()));
    public static final DecodeJson<UUID> DUUID = DString.tryMap(s -> () -> UUID.fromString(s));
    public static final DecodeJson<URI> DURI = DString.tryMap(s -> () -> URI.create(s));
    public static final DecodeJson<URL> DURL = DURI.tryMap(uri -> uri::toURL);
    public static final DecodeJson<ZonedDateTime> DISODateTimeUTC = zonedDateTimeDecoder(DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC));
    public static final DecodeJson<Instant> DISOInstantUTC = instantDecoder(DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC));

    public static DecodeJson<ZonedDateTime> zonedDateTimeDecoder(DateTimeFormatter formatter) {
        return DString.tryMap(s -> () -> ZonedDateTime.parse(s, formatter));
    }

    public static DecodeJson<Instant> instantDecoder(DateTimeFormatter formatter) {
        return DString.tryMap(s -> () -> formatter.parse(s, Instant::from));
    }

    public static <A> DecodeJson<List<A>> listDecoder(DecodeJson<A> decoder) {
        DecodeJson<List<A>> outer = value -> DecodeResult.sequence(value.asJsonArrayOrEmpty().mapToList(decoder::fromJson));
        return outer.withDefaultValue(List.of());
    }

    public static <A> DecodeJson<Set<A>> setDecoder(DecodeJson<A> decoder) {
        return listDecoder(decoder).map(Set::copyOf).withDefaultValue(Set.of());
    }

    public static <A> DecodeJson<Optional<A>> optionalDecoder(DecodeJson<A> decoder) {
        DecodeJson<Optional<A>> outerDecoder = value -> value.isNull() ? DecodeResult.ok(Optional.empty()) : DecodeResult.ok(decoder.fromJson(value).toOption());
        return outerDecoder.withDefaultValue(Optional.empty());
    }

    public static <A> DecodeJson<A> objectDecoder(Function<Json.JObject, DecodeResult<A>> decoder) {
        return json -> decoder.apply(json.asJsonObjectOrEmpty());
    }


    public static <TT, A> DecodeJson<TT> decode(FieldDecoder<A> Function, Function<A, TT> func) {
        return (value) -> {
            var oa = DecodeResult.decode(value.asJsonObjectOrEmpty(), Function);
            return oa.flatMap(a -> DecodeResult.ok(func.apply(a)));
        };
    }

    public static <TT, A1, A2> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, BiFunction<A1, A2, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            return d1.flatMap(v1 -> d2.flatMap(v2 ->  DecodeResult.ok(func.apply(v1, v2))));
        };
    }

    public static <TT, A1, A2, A3> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, Function3<A1, A2, A3, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 ->  DecodeResult.ok(func.apply(v1, v2, v3)))));
        };
    }

    public static <TT, A1, A2, A3, A4> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, Function4<A1, A2, A3, A4, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, Function5<A1, A2, A3, A4, A5, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5)))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, Function6<A1, A2, A3, A4, A5, A6, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, Function7<A1, A2, A3, A4, A5, A6, A7, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7)))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, Function8<A1, A2, A3, A4, A5, A6, A7, A8, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, Function9<A1, A2, A3, A4, A5, A6, A7, A8, A9, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9)))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, Function10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, Function11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, Function12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, Function13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, Function14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, Function15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, Function16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            var d16 = DecodeResult.decode(object, fd16);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, Function17<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            var d16 = DecodeResult.decode(object, fd16);
            var d17 = DecodeResult.decode(object, fd17);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, Function18<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            var d16 = DecodeResult.decode(object, fd16);
            var d17 = DecodeResult.decode(object, fd17);
            var d18 = DecodeResult.decode(object, fd18);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, Function19<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            var d16 = DecodeResult.decode(object, fd16);
            var d17 = DecodeResult.decode(object, fd17);
            var d18 = DecodeResult.decode(object, fd18);
            var d19 = DecodeResult.decode(object, fd19);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, Function20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            var d16 = DecodeResult.decode(object, fd16);
            var d17 = DecodeResult.decode(object, fd17);
            var d18 = DecodeResult.decode(object, fd18);
            var d19 = DecodeResult.decode(object, fd19);
            var d20 = DecodeResult.decode(object, fd20);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, Function21<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            var d16 = DecodeResult.decode(object, fd16);
            var d17 = DecodeResult.decode(object, fd17);
            var d18 = DecodeResult.decode(object, fd18);
            var d19 = DecodeResult.decode(object, fd19);
            var d20 = DecodeResult.decode(object, fd20);
            var d21 = DecodeResult.decode(object, fd21);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, FieldDecoder<A22> fd22, Function22<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            var d16 = DecodeResult.decode(object, fd16);
            var d17 = DecodeResult.decode(object, fd17);
            var d18 = DecodeResult.decode(object, fd18);
            var d19 = DecodeResult.decode(object, fd19);
            var d20 = DecodeResult.decode(object, fd20);
            var d21 = DecodeResult.decode(object, fd21);
            var d22 = DecodeResult.decode(object, fd22);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, FieldDecoder<A22> fd22, FieldDecoder<A23> fd23, Function23<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            var d16 = DecodeResult.decode(object, fd16);
            var d17 = DecodeResult.decode(object, fd17);
            var d18 = DecodeResult.decode(object, fd18);
            var d19 = DecodeResult.decode(object, fd19);
            var d20 = DecodeResult.decode(object, fd20);
            var d21 = DecodeResult.decode(object, fd21);
            var d22 = DecodeResult.decode(object, fd22);
            var d23 = DecodeResult.decode(object, fd23);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23)))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, FieldDecoder<A22> fd22, FieldDecoder<A23> fd23, FieldDecoder<A24> fd24, Function24<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            var d16 = DecodeResult.decode(object, fd16);
            var d17 = DecodeResult.decode(object, fd17);
            var d18 = DecodeResult.decode(object, fd18);
            var d19 = DecodeResult.decode(object, fd19);
            var d20 = DecodeResult.decode(object, fd20);
            var d21 = DecodeResult.decode(object, fd21);
            var d22 = DecodeResult.decode(object, fd22);
            var d23 = DecodeResult.decode(object, fd23);
            var d24 = DecodeResult.decode(object, fd24);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24))))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, FieldDecoder<A22> fd22, FieldDecoder<A23> fd23, FieldDecoder<A24> fd24, FieldDecoder<A25> fd25, Function25<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            var d16 = DecodeResult.decode(object, fd16);
            var d17 = DecodeResult.decode(object, fd17);
            var d18 = DecodeResult.decode(object, fd18);
            var d19 = DecodeResult.decode(object, fd19);
            var d20 = DecodeResult.decode(object, fd20);
            var d21 = DecodeResult.decode(object, fd21);
            var d22 = DecodeResult.decode(object, fd22);
            var d23 = DecodeResult.decode(object, fd23);
            var d24 = DecodeResult.decode(object, fd24);
            var d25 = DecodeResult.decode(object, fd25);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 -> d25.flatMap(v25 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25)))))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, FieldDecoder<A22> fd22, FieldDecoder<A23> fd23, FieldDecoder<A24> fd24, FieldDecoder<A25> fd25, FieldDecoder<A26> fd26, Function26<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            var d16 = DecodeResult.decode(object, fd16);
            var d17 = DecodeResult.decode(object, fd17);
            var d18 = DecodeResult.decode(object, fd18);
            var d19 = DecodeResult.decode(object, fd19);
            var d20 = DecodeResult.decode(object, fd20);
            var d21 = DecodeResult.decode(object, fd21);
            var d22 = DecodeResult.decode(object, fd22);
            var d23 = DecodeResult.decode(object, fd23);
            var d24 = DecodeResult.decode(object, fd24);
            var d25 = DecodeResult.decode(object, fd25);
            var d26 = DecodeResult.decode(object, fd26);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 -> d25.flatMap(v25 -> d26.flatMap(v26 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26))))))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27> DecodeJson<TT> decode(FieldDecoder<A1> fd1, FieldDecoder<A2> fd2, FieldDecoder<A3> fd3, FieldDecoder<A4> fd4, FieldDecoder<A5> fd5, FieldDecoder<A6> fd6, FieldDecoder<A7> fd7, FieldDecoder<A8> fd8, FieldDecoder<A9> fd9, FieldDecoder<A10> fd10, FieldDecoder<A11> fd11, FieldDecoder<A12> fd12, FieldDecoder<A13> fd13, FieldDecoder<A14> fd14, FieldDecoder<A15> fd15, FieldDecoder<A16> fd16, FieldDecoder<A17> fd17, FieldDecoder<A18> fd18, FieldDecoder<A19> fd19, FieldDecoder<A20> fd20, FieldDecoder<A21> fd21, FieldDecoder<A22> fd22, FieldDecoder<A23> fd23, FieldDecoder<A24> fd24, FieldDecoder<A25> fd25, FieldDecoder<A26> fd26, FieldDecoder<A27> fd27, Function27<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27, TT> func) {
        return (value) -> {
            var object = value.asJsonObjectOrEmpty();
            var d1 = DecodeResult.decode(object, fd1);
            var d2 = DecodeResult.decode(object, fd2);
            var d3 = DecodeResult.decode(object, fd3);
            var d4 = DecodeResult.decode(object, fd4);
            var d5 = DecodeResult.decode(object, fd5);
            var d6 = DecodeResult.decode(object, fd6);
            var d7 = DecodeResult.decode(object, fd7);
            var d8 = DecodeResult.decode(object, fd8);
            var d9 = DecodeResult.decode(object, fd9);
            var d10 = DecodeResult.decode(object, fd10);
            var d11 = DecodeResult.decode(object, fd11);
            var d12 = DecodeResult.decode(object, fd12);
            var d13 = DecodeResult.decode(object, fd13);
            var d14 = DecodeResult.decode(object, fd14);
            var d15 = DecodeResult.decode(object, fd15);
            var d16 = DecodeResult.decode(object, fd16);
            var d17 = DecodeResult.decode(object, fd17);
            var d18 = DecodeResult.decode(object, fd18);
            var d19 = DecodeResult.decode(object, fd19);
            var d20 = DecodeResult.decode(object, fd20);
            var d21 = DecodeResult.decode(object, fd21);
            var d22 = DecodeResult.decode(object, fd22);
            var d23 = DecodeResult.decode(object, fd23);
            var d24 = DecodeResult.decode(object, fd24);
            var d25 = DecodeResult.decode(object, fd25);
            var d26 = DecodeResult.decode(object, fd26);
            var d27 = DecodeResult.decode(object, fd27);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 -> d25.flatMap(v25 -> d26.flatMap(v26 -> d27.flatMap(v27 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27)))))))))))))))))))))))))))));
        };
    }
}
