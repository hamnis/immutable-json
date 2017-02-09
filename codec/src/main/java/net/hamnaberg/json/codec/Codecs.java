package net.hamnaberg.json.codec;

import javaslang.*;
import javaslang.collection.List;
import javaslang.control.Option;
import net.hamnaberg.json.Json;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class Codecs {
    private Codecs(){}

    public static final JsonCodec<String> CString = new DefaultJsonCodec<>(
            Decoders.DString,
            Encoders.EString,
            "String"
    );

    public static final JsonCodec<Number> CNumber = new DefaultJsonCodec<>(
            Decoders.DNumber,
            Encoders.ENumber,
            "Number"
    );

    public static final JsonCodec<Long> CLong = new DefaultJsonCodec<>(
            Decoders.DLong,
            Encoders.ELong,
            "Long"
    );

    public static final JsonCodec<Double> CDouble = new DefaultJsonCodec<>(
            Decoders.DDouble,
            Encoders.EDouble,
            "Double"
    );

    public static final JsonCodec<Integer> CInt = new DefaultJsonCodec<>(
            Decoders.DInt,
            Encoders.EInt,
            "Int"
    );

    public static final JsonCodec<Boolean> CBoolean = new DefaultJsonCodec<>(
            Decoders.DBoolean,
            Encoders.EBoolean,
            "Boolean"
    );

    public static <A> JsonCodec<A> nullCodec() {
        return new DefaultJsonCodec<>(
                ignore -> DecodeResult.ok(null),
                ignore -> Json.jNull(),
                "Null"
        );
    }

    public static <A> JsonCodec<List<A>> listCodec(JsonCodec<A> codec) {
        DecodeJson<List<A>> decodeJson = value -> DecodeResult.sequence(value.asJsonArrayOrEmpty().mapToList(codec::fromJson));
        return new DefaultJsonCodec<>(
                decodeJson.withDefaultValue(List.empty()),
                value -> Json.jArray(value.map(codec::toJson)),
                String.format("ListCodec(%s)", codec.toString())
        );
    }

    public static <A> JsonCodec<java.util.List<A>> javaListCodec(JsonCodec<A> codec) {
        JsonCodec<java.util.List<A>> listCodec = listCodec(codec).xmap(List::toJavaList, List::ofAll);
        return JsonCodec.lift(listCodec.withDefaultValue(Collections.emptyList()), listCodec);
    }

    public static <A> JsonCodec<Option<A>> OptionCodec(JsonCodec<A> codec) {
        DecodeJson<Option<A>> decoder = value -> value.isNull() ? DecodeResult.ok(Option.none()) : DecodeResult.ok(codec.fromJson(value).toOption());
        EncodeJson<Option<A>> encoder = value -> value.map(codec::toJson).getOrElse(Json.jNull());
        return JsonCodec.lift(decoder.withDefaultValue(Option.none()), encoder);
    }

    public static <A> JsonCodec<Optional<A>> OptionalCodec(JsonCodec<A> underlying) {
        JsonCodec<Optional<A>> codec = OptionCodec(underlying).xmap(Option::toJavaOptional, Option::ofOptional);
        return JsonCodec.lift(codec.withDefaultValue(Optional.empty()), codec);
    }

    public static <A> JsonCodec<A> objectCodec(Function<Json.JObject, DecodeResult<A>> decoder, Function<A, Json.JObject> encoder) {
        return JsonCodec.lift(
                json -> decoder.apply(json.asJsonObjectOrEmpty()),
                a ->encoder.apply(a).asJValue()
        );
    }

    public static <A1> JsonCodec<A1> of(NamedJsonCodec<A1> c1) {
        return codec(c1);
    }

    public static <A1, A2> JsonCodec<Tuple2<A1, A2>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2) {
        return codec(Iso.identity(), c1, c2);
    }

    public static <A1, A2, A3> JsonCodec<Tuple3<A1, A2, A3>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3) {
        return codec(Iso.identity(), c1, c2, c3);
    }

    public static <A1, A2, A3, A4> JsonCodec<Tuple4<A1, A2, A3, A4>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4) {
        return codec(Iso.identity(), c1, c2, c3, c4);
    }

    public static <A1, A2, A3, A4, A5> JsonCodec<Tuple5<A1, A2, A3, A4, A5>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5);
    }

    public static <A1, A2, A3, A4, A5, A6> JsonCodec<Tuple6<A1, A2, A3, A4, A5, A6>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6);
    }

    public static <A1, A2, A3, A4, A5, A6, A7> JsonCodec<Tuple7<A1, A2, A3, A4, A5, A6, A7>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7);
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8> JsonCodec<Tuple8<A1, A2, A3, A4, A5, A6, A7, A8>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8);
    }

    public static <TT> JsonCodec<TT> codec(NamedJsonCodec<TT> c1) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                return Json.jObject(
                        c1.name,
                        c1.toJson(value)
                ).asJValue();
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();

                return DecodeResult.decode(object, c1.name, c1);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());

                return "codec" + map.toString();
            }
        };
    }

    public static <TT, A, B> JsonCodec<TT> codec(Iso<TT, Tuple2<A, B>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple2<A, B> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();

                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                return oa.flatMap(a -> ob.flatMap(b -> DecodeResult.ok(iso.reverseGet(new Tuple2<>(a,b)))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());

                return "codec" + map.toString();
            }
        };
    }



    public static <TT, A, B, C> JsonCodec<TT> codec(Iso<TT, Tuple3<A, B, C>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple3<A, B, C> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<C> oc = DecodeResult.decode(object, c3.name, c3);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> DecodeResult.ok(iso.reverseGet(new Tuple3<>(a,b,c))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());

                return "codec" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D> JsonCodec<TT> codec(Iso<TT, Tuple4<A, B, C, D>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple4<A, B, C, D> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<C> oc = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<D> od = DecodeResult.decode(object, c4.name, c4);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> DecodeResult.ok(iso.reverseGet(new Tuple4<>(a,b,c,d)))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());

                return "codec" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D, E> JsonCodec<TT> codec(Iso<TT, Tuple5<A, B, C, D, E>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4, NamedJsonCodec<E> c5) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple5<A, B, C, D, E> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<C> oc = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<D> od = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<E> oe = DecodeResult.decode(object, c5.name, c5);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> DecodeResult.ok(iso.reverseGet(new Tuple5<>(a,b,c,d,e))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());

                return "codec" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D, E, F> JsonCodec<TT> codec(Iso<TT, Tuple6<A, B, C, D, E, F>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4, NamedJsonCodec<E> c5, NamedJsonCodec<F> c6) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple6<A, B, C, D, E, F> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<C> oc = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<D> od = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<E> oe = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<F> of = DecodeResult.decode(object, c6.name, c6);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> DecodeResult.ok(iso.reverseGet(new Tuple6<>(a,b,c,d,e,f)))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());

                return "codec" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D, E, F, G> JsonCodec<TT> codec(Iso<TT, Tuple7<A, B, C, D, E, F, G>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4, NamedJsonCodec<E> c5, NamedJsonCodec<F> c6, NamedJsonCodec<G> c7) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple7<A, B, C, D, E, F, G> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<C> oc = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<D> od = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<E> oe = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<F> of = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<G> og = DecodeResult.decode(object, c7.name, c7);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> DecodeResult.ok(iso.reverseGet(new Tuple7<>(a,b,c,d,e,f,g))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());

                return "codec" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D, E, F, G, H> JsonCodec<TT> codec(Iso<TT, Tuple8<A, B, C, D, E, F, G, H>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4, NamedJsonCodec<E> c5, NamedJsonCodec<F> c6, NamedJsonCodec<G> c7, NamedJsonCodec<H> c8) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple8<A, B, C, D, E, F, G, H> tuple = iso.get(value);

                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<C> oc = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<D> od = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<E> oe = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<F> of = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<G> og = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<H> oh = DecodeResult.decode(object, c8.name, c8);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> DecodeResult.ok(iso.reverseGet(new Tuple8<>(a,b,c,d,e,f,g,h)))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());

                return "codec" + map.toString();
            }
        };
    }
}
