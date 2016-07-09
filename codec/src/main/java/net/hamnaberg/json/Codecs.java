package net.hamnaberg.json;

import javaslang.*;
import javaslang.collection.List;
import javaslang.control.Option;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static net.hamnaberg.json.DecodeResult.decode;

public abstract class Codecs {
    private Codecs(){}

    public static final JsonCodec<String> StringCodec = new DefaultJsonCodec<>(
            value -> DecodeResult.fromOption(value.asString()),
            value -> Option.some(Json.jString(value)),
            "StringCodec"
    );

    public static final JsonCodec<Number> numberCodec = new DefaultJsonCodec<>(
            value -> DecodeResult.fromOption(value.asBigDecimal().map(v -> (Number) v)),
            value -> Option.of(Json.jNumber(value)),
            "NumberCodec"
    );

    public static final JsonCodec<Long> longCodec = new DefaultJsonCodec<>(
            value -> DecodeResult.fromOption(value.asJsonNumber().map(Json.JNumber::asLong)),
            value -> Option.of(Json.jNumber(value)),
            "LongCodec"
    );

    public static final JsonCodec<Double> doubleCodec = new DefaultJsonCodec<>(
            value -> DecodeResult.fromOption(value.asJsonNumber().map(Json.JNumber::asDouble)),
            value -> Option.of(Json.jNumber(value)),
            "DoubleCodec"
    );

    public static final JsonCodec<Integer> intCodec = new DefaultJsonCodec<>(
            value -> DecodeResult.fromOption(value.asJsonNumber().map(Json.JNumber::asInt)),
            value -> Option.of(Json.jNumber(value)),
            "IntCodec"
    );

    public static final JsonCodec<Boolean> booleanCodec = new DefaultJsonCodec<>(
            value -> DecodeResult.fromOption(value.asBoolean()),
            value -> Option.of(Json.jBoolean(value)),
            "BooleanCodec"
    );

    public static <A> JsonCodec<A> nullCodec() {
        return new DefaultJsonCodec<>(
                ignore -> DecodeResult.ok(null),
                ignore -> Option.of(Json.jNull()),
                "NullCodec"
        );
    }

    public static <A> JsonCodec<List<A>> listCodec(JsonCodec<A> codec) {
        DecodeJson<List<A>> decodeJson = value -> DecodeResult.sequence(value.asJsonArrayOrEmpty().mapToList(codec::fromJson));
        return new DefaultJsonCodec<>(
                decodeJson.withDefaultValue(List.empty()),
                value -> Option.of(Json.jArray(value.flatMap(a -> codec.toJson(a).toList()))),
                String.format("ListCodec(%s)", codec.toString())
        );
    }

    public static <A> JsonCodec<java.util.List<A>> javaListCodec(JsonCodec<A> codec) {
        JsonCodec<java.util.List<A>> listCodec = listCodec(codec).xmap(List::toJavaList, List::ofAll);
        return JsonCodec.lift(listCodec.withDefaultValue(Collections.emptyList()), listCodec);
    }

    public static <A> JsonCodec<Option<A>> OptionCodec(JsonCodec<A> codec) {
        DecodeJson<Option<A>> decoder = value -> value.isNull() ? DecodeResult.ok(Option.none()) : DecodeResult.ok(codec.fromJson(value).toOption());
        EncodeJson<Option<A>> encoder = value -> value.flatMap(codec::toJson).orElse(Option.some(Json.jNull()));
        return JsonCodec.lift(decoder.withDefaultValue(Option.none()), encoder);
    }

    public static <A> JsonCodec<Optional<A>> OptionalCodec(JsonCodec<A> underlying) {
        JsonCodec<Optional<A>> codec = OptionCodec(underlying).xmap(Option::toJavaOptional, Option::ofOptional);
        return JsonCodec.lift(codec.withDefaultValue(Optional.empty()), codec);
    }

    public static <A> JsonCodec<A> objectCodec(Function<Json.JObject, DecodeResult<A>> decoder, Function<A, Json.JObject> encoder) {
        return JsonCodec.lift(
                json -> decoder.apply(json.asJsonObjectOrEmpty()),
                a -> Option.some(encoder.apply(a).asJValue())
        );
    }

    @Deprecated
    /**
     * @deprecated Use {@link JsonCodec#lift(DecodeJson, EncodeJson)} instead
     */
    public static <A> JsonCodec<A> makeCodec(DecodeJson<A> decoder, EncodeJson<A> encoder) {
        return JsonCodec.lift(decoder, encoder);
    }

    public static <TT, A> JsonCodec<TT> codec1(Iso<TT, Tuple1<A>> iso, NamedJsonCodec<A> c1) {
        return new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple1<A> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> Option.of(Json.jObject(Json.entry(c1.name(), j1))));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();

                DecodeResult<A> oa = decode(object, c1.name(), c1);
                return oa.flatMap(a -> DecodeResult.ok(iso.reverseGet(new Tuple1<>(a))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name(), c1.toString());

                return "codec1" + map.toString();
            }
        };
    }



    public static <TT, A, B> JsonCodec<TT> codec2(Iso<TT, Tuple2<A, B>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2) {
        return new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple2<A, B> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> Option.of(Json.jObject(Json.entry(c1.name(), j1),Json.entry(c2.name(), j2)))));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();

                DecodeResult<A> oa = decode(object, c1.name(), c1);
                DecodeResult<B> ob = decode(object, c2.name(), c2);
                return oa.flatMap(a -> ob.flatMap(b -> DecodeResult.ok(iso.reverseGet(new Tuple2<>(a,b)))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name(), c1.toString());
                map.put(c2.name(), c2.toString());

                return "codec2" + map.toString();
            }
        };
    }



    public static <TT, A, B, C> JsonCodec<TT> codec3(Iso<TT, Tuple3<A, B, C>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3) {
        return new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple3<A, B, C> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> Option.of(Json.jObject(Json.entry(c1.name(), j1),Json.entry(c2.name(), j2),Json.entry(c3.name(), j3))))));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = decode(object, c1.name(), c1);
                DecodeResult<B> ob = decode(object, c2.name(), c2);
                DecodeResult<C> oc = decode(object, c3.name(), c3);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> DecodeResult.ok(iso.reverseGet(new Tuple3<>(a,b,c))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name(), c1.toString());
                map.put(c2.name(), c2.toString());
                map.put(c3.name(), c3.toString());

                return "codec3" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D> JsonCodec<TT> codec4(Iso<TT, Tuple4<A, B, C, D>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4) {
        return new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple4<A, B, C, D> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> Option.of(Json.jObject(Json.entry(c1.name(), j1),Json.entry(c1.name(), j2),Json.entry(c3.name(), j3),Json.entry(c4.name(), j4)))))));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = decode(object, c1.name(), c1);
                DecodeResult<B> ob = decode(object, c2.name(), c2);
                DecodeResult<C> oc = decode(object, c3.name(), c3);
                DecodeResult<D> od = decode(object, c4.name(), c4);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> DecodeResult.ok(iso.reverseGet(new Tuple4<>(a,b,c,d)))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name(), c1.toString());
                map.put(c2.name(), c2.toString());
                map.put(c3.name(), c3.toString());
                map.put(c4.name(), c4.toString());

                return "codec4" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D, E> JsonCodec<TT> codec5(Iso<TT, Tuple5<A, B, C, D, E>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4, NamedJsonCodec<E> c5) {
        return new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple5<A, B, C, D, E> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> Option.of(Json.jObject(Json.entry(c1.name(), j1),Json.entry(c1.name(), j2),Json.entry(c3.name(), j3),Json.entry(c4.name(), j4),Json.entry(c5.name(), j5))))))));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = decode(object, c1.name(), c1);
                DecodeResult<B> ob = decode(object, c2.name(), c2);
                DecodeResult<C> oc = decode(object, c3.name(), c3);
                DecodeResult<D> od = decode(object, c4.name(), c4);
                DecodeResult<E> oe = decode(object, c5.name(), c5);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> DecodeResult.ok(iso.reverseGet(new Tuple5<>(a,b,c,d,e))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name(), c1.toString());
                map.put(c2.name(), c2.toString());
                map.put(c3.name(), c3.toString());
                map.put(c4.name(), c4.toString());
                map.put(c5.name(), c5.toString());

                return "codec5" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D, E, F> JsonCodec<TT> codec6(Iso<TT, Tuple6<A, B, C, D, E, F>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4, NamedJsonCodec<E> c5, NamedJsonCodec<F> c6) {
        return new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple6<A, B, C, D, E, F> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> Option.of(Json.jObject(Json.entry(c1.name(), j1),Json.entry(c2.name(), j2),Json.entry(c3.name(), j3),Json.entry(c4.name(), j4),Json.entry(c5.name(), j5),Json.entry(c6.name(), j6)))))))));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = decode(object, c1.name(), c1);
                DecodeResult<B> ob = decode(object, c2.name(), c2);
                DecodeResult<C> oc = decode(object, c3.name(), c3);
                DecodeResult<D> od = decode(object, c4.name(), c4);
                DecodeResult<E> oe = decode(object, c5.name(), c5);
                DecodeResult<F> of = decode(object, c6.name(), c6);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> DecodeResult.ok(iso.reverseGet(new Tuple6<>(a,b,c,d,e,f)))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name(), c1.toString());
                map.put(c2.name(), c2.toString());
                map.put(c3.name(), c3.toString());
                map.put(c4.name(), c4.toString());
                map.put(c5.name(), c5.toString());
                map.put(c6.name(), c6.toString());

                return "codec6" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D, E, F, G> JsonCodec<TT> codec7(Iso<TT, Tuple7<A, B, C, D, E, F, G>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4, NamedJsonCodec<E> c5, NamedJsonCodec<F> c6, NamedJsonCodec<G> c7) {
        return new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple7<A, B, C, D, E, F, G> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).map(j7 -> Json.jObject(Json.entry(c1.name(), j1),Json.entry(c2.name(), j2),Json.entry(c3.name(), j3),Json.entry(c4.name(), j4),Json.entry(c5.name(), j5),Json.entry(c6.name(), j6),Json.entry(c7.name(), j7)))))))));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = decode(object, c1.name(), c1);
                DecodeResult<B> ob = decode(object, c2.name(), c2);
                DecodeResult<C> oc = decode(object, c3.name(), c3);
                DecodeResult<D> od = decode(object, c4.name(), c4);
                DecodeResult<E> oe = decode(object, c5.name(), c5);
                DecodeResult<F> of = decode(object, c6.name(), c6);
                DecodeResult<G> og = decode(object, c7.name(), c7);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> DecodeResult.ok(iso.reverseGet(new Tuple7<>(a,b,c,d,e,f,g))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name(), c1.toString());
                map.put(c2.name(), c2.toString());
                map.put(c3.name(), c3.toString());
                map.put(c4.name(), c4.toString());
                map.put(c5.name(), c5.toString());
                map.put(c6.name(), c6.toString());
                map.put(c7.name(), c7.toString());

                return "codec7" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D, E, F, G, H> JsonCodec<TT> codec8(Iso<TT, Tuple8<A, B, C, D, E, F, G, H>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4, NamedJsonCodec<E> c5, NamedJsonCodec<F> c6, NamedJsonCodec<G> c7, NamedJsonCodec<H> c8) {
        return new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple8<A, B, C, D, E, F, G, H> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> Option.of(Json.jObject(Json.entry(c1.name(), j1),Json.entry(c2.name(), j2),Json.entry(c3.name(), j3),Json.entry(c4.name(), j4),Json.entry(c5.name(), j5),Json.entry(c6.name(), j6),Json.entry(c7.name(), j7),Json.entry(c8.name(), j8)))))))))));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = decode(object, c1.name(), c1);
                DecodeResult<B> ob = decode(object, c2.name(), c2);
                DecodeResult<C> oc = decode(object, c3.name(), c3);
                DecodeResult<D> od = decode(object, c4.name(), c4);
                DecodeResult<E> oe = decode(object, c5.name(), c5);
                DecodeResult<F> of = decode(object, c6.name(), c6);
                DecodeResult<G> og = decode(object, c7.name(), c7);
                DecodeResult<H> oh = decode(object, c8.name(), c8);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> DecodeResult.ok(iso.reverseGet(new Tuple8<>(a,b,c,d,e,f,g,h)))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name(), c1.toString());
                map.put(c2.name(), c2.toString());
                map.put(c3.name(), c3.toString());
                map.put(c4.name(), c4.toString());
                map.put(c5.name(), c5.toString());
                map.put(c6.name(), c6.toString());
                map.put(c7.name(), c7.toString());
                map.put(c8.name(), c8.toString());

                return "codec8" + map.toString();
            }
        };
    }
}
