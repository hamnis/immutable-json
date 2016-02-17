package net.hamnaberg.json;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class Codecs {
    private Codecs(){}
    public static final JsonCodec<String> StringCodec = new JsonCodec<String>() {
        @Override
        public Optional<Json.JValue> toJson(String value) {
            return Optional.of(Json.jString(value));
        }

        @Override
        public Optional<String> fromJson(Json.JValue value) {
            return value.asString();
        }
    };

    public static final JsonCodec<Number> numberCodec = new JsonCodec<Number>() {
        @Override
        public Optional<Json.JValue> toJson(Number value) {
            return Optional.of(Json.jNumber(value));
        }

        @Override
        public Optional<Number> fromJson(Json.JValue value) {
            return value.asBigDecimal().map(v -> (Number) v);
        }
    };

    public static final JsonCodec<Long> longCodec = new JsonCodec<Long>() {
        @Override
        public Optional<Json.JValue> toJson(Long value) {
            return Optional.of(Json.jNumber(value));
        }

        @Override
        public Optional<Long> fromJson(Json.JValue value) {
            return value.asJsonNumber().map(Json.JNumber::asLong);
        }
    };

    public static final JsonCodec<Double> doubleCodec = new JsonCodec<Double>() {
        @Override
        public Optional<Json.JValue> toJson(Double value) {
            return Optional.of(Json.jNumber(value));
        }

        @Override
        public Optional<Double> fromJson(Json.JValue value) {
            return value.asJsonNumber().map(Json.JNumber::asDouble);
        }
    };

    public static final JsonCodec<Integer> intCodec = new JsonCodec<Integer>() {
        @Override
        public Optional<Json.JValue> toJson(Integer value) {
            return Optional.of(Json.jNumber(value));
        }

        @Override
        public Optional<Integer> fromJson(Json.JValue value) {
            return value.asJsonNumber().map(Json.JNumber::asInt);
        }
    };

    public static final JsonCodec<Boolean> booleanCodec = new JsonCodec<Boolean>() {
        @Override
        public Optional<Json.JValue> toJson(Boolean value) {
            return Optional.of(Json.jBoolean(value));
        }

        @Override
        public Optional<Boolean> fromJson(Json.JValue value) {
            return value.asBoolean();
        }
    };

    public static <A> JsonCodec<A> nullCodec() {
        return new JsonCodec<A>() {
            @Override
            public Optional<Json.JValue> toJson(A value) {
                return Optional.of(Json.jNull());
            }

            @Override
            public Optional<A> fromJson(Json.JValue value) {
                return Optional.empty();
            }
        };
    }

    public static <A> JsonCodec<List<A>> listCodec(JsonCodec<A> codec) {
        return new JsonCodec<List<A>>() {
            @Override
            public Optional<List<A>> fromJson(Json.JValue value) {
                return value.asJsonArray().map(j -> j.mapOpt(codec::fromJson));
            }

            @Override
            public Optional<Json.JValue> toJson(List<A> value) {
                return Optional.of(Json.jArray(value.stream().flatMap(a -> {
                    Optional<Json.JValue> jv = codec.toJson(a);
                    return jv.isPresent() ? Stream.of(jv.get()) : Stream.empty();
                }).collect(Collectors.toList())));
            }
        };
    }

    public static <A> JsonCodec<Optional<A>> optionalCodec(JsonCodec<A> codec) {
        return new JsonCodec<Optional<A>>() {
            @Override
            public Optional<Optional<A>> fromJson(Json.JValue value) {
                return Optional.of(codec.fromJson(value));
            }

            @Override
            public Optional<Json.JValue> toJson(Optional<A> value) {
                return value.flatMap(v -> codec.toJson(v));
            }
        };
    }


    public static <TT, A> javaslang.Function1<String, JsonCodec<TT>> codec1(Iso<TT, javaslang.Tuple1<A>> iso, JsonCodec<A> c1) {
        return (n1) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple1<A> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> Optional.of(Json.jObject(Json.entry(n1, j1))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                return oa.flatMap(a -> Optional.of(iso.reverseGet(new javaslang.Tuple1<>(a))));
            }
        };
    }


    public static <TT, A, B> javaslang.Function2<String, String, JsonCodec<TT>> codec2(Iso<TT, javaslang.Tuple2<A, B>> iso, JsonCodec<A> c1, JsonCodec<B> c2) {
        return (n1, n2) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple2<A, B> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2)))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> Optional.of(iso.reverseGet(new javaslang.Tuple2<>(a, b)))));
            }
        };
    }


    public static <TT, A, B, C> javaslang.Function3<String, String, String, JsonCodec<TT>> codec3(Iso<TT, javaslang.Tuple3<A, B, C>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3) {
        return (n1, n2, n3) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple3<A, B, C> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> Optional.of(iso.reverseGet(new javaslang.Tuple3<>(a, b, c))))));
            }
        };
    }


    public static <TT, A, B, C, D> javaslang.Function4<String, String, String, String, JsonCodec<TT>> codec4(Iso<TT, javaslang.Tuple4<A, B, C, D>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4) {
        return (n1, n2, n3, n4) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple4<A, B, C, D> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4)))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> Optional.of(iso.reverseGet(new javaslang.Tuple4<>(a, b, c, d)))))));
            }
        };
    }


    public static <TT, A, B, C, D, E> javaslang.Function5<String, String, String, String, String, JsonCodec<TT>> codec5(Iso<TT, javaslang.Tuple5<A, B, C, D, E>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5) {
        return (n1, n2, n3, n4, n5) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple5<A, B, C, D, E> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> Optional.of(iso.reverseGet(new javaslang.Tuple5<>(a, b, c, d, e))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F> javaslang.Function6<String, String, String, String, String, String, JsonCodec<TT>> codec6(Iso<TT, javaslang.Tuple6<A, B, C, D, E, F>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6) {
        return (n1, n2, n3, n4, n5, n6) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple6<A, B, C, D, E, F> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6)))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> Optional.of(iso.reverseGet(new javaslang.Tuple6<>(a, b, c, d, e, f)))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G> javaslang.Function7<String, String, String, String, String, String, String, JsonCodec<TT>> codec7(Iso<TT, javaslang.Tuple7<A, B, C, D, E, F, G>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7) {
        return (n1, n2, n3, n4, n5, n6, n7) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple7<A, B, C, D, E, F, G> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> Optional.of(iso.reverseGet(new javaslang.Tuple7<>(a, b, c, d, e, f, g))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H> javaslang.Function8<String, String, String, String, String, String, String, String, JsonCodec<TT>> codec8(Iso<TT, javaslang.Tuple8<A, B, C, D, E, F, G, H>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8) {
        return (n1, n2, n3, n4, n5, n6, n7, n8) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple8<A, B, C, D, E, F, G, H> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8)))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> Optional.of(iso.reverseGet(new javaslang.Tuple8<>(a, b, c, d, e, f, g, h)))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I> javaslang.Function9<String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec9(Iso<TT, javaslang.Tuple9<A, B, C, D, E, F, G, H, I>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple9<A, B, C, D, E, F, G, H, I> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> Optional.of(iso.reverseGet(new javaslang.Tuple9<>(a, b, c, d, e, f, g, h, i))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J> javaslang.Function10<String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec10(Iso<TT, javaslang.Tuple10<A, B, C, D, E, F, G, H, I, J>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple10<A, B, C, D, E, F, G, H, I, J> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10)))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> Optional.of(iso.reverseGet(new javaslang.Tuple10<>(a, b, c, d, e, f, g, h, i, j)))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J, K> javaslang.Function11<String, String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec11(Iso<TT, javaslang.Tuple11<A, B, C, D, E, F, G, H, I, J, K>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10, JsonCodec<K> c11) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple11<A, B, C, D, E, F, G, H, I, J, K> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> c11.toJson(tuple._11).flatMap(j11 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10), Json.entry(n11, j11))))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                Optional<K> ok = object.getAs(n11, c11::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> Optional.of(iso.reverseGet(new javaslang.Tuple11<>(a, b, c, d, e, f, g, h, i, j, k))))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L> javaslang.Function12<String, String, String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec12(Iso<TT, javaslang.Tuple12<A, B, C, D, E, F, G, H, I, J, K, L>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10, JsonCodec<K> c11, JsonCodec<L> c12) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> c11.toJson(tuple._11).flatMap(j11 -> c12.toJson(tuple._12).flatMap(j12 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10), Json.entry(n11, j11), Json.entry(n12, j12)))))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                Optional<K> ok = object.getAs(n11, c11::fromJson);
                Optional<L> ol = object.getAs(n12, c12::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> Optional.of(iso.reverseGet(new javaslang.Tuple12<>(a, b, c, d, e, f, g, h, i, j, k, l)))))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M> javaslang.Function13<String, String, String, String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec13(Iso<TT, javaslang.Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10, JsonCodec<K> c11, JsonCodec<L> c12, JsonCodec<M> c13) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> c11.toJson(tuple._11).flatMap(j11 -> c12.toJson(tuple._12).flatMap(j12 -> c13.toJson(tuple._13).flatMap(j13 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10), Json.entry(n11, j11), Json.entry(n12, j12), Json.entry(n13, j13))))))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                Optional<K> ok = object.getAs(n11, c11::fromJson);
                Optional<L> ol = object.getAs(n12, c12::fromJson);
                Optional<M> om = object.getAs(n13, c13::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> Optional.of(iso.reverseGet(new javaslang.Tuple13<>(a, b, c, d, e, f, g, h, i, j, k, l, m))))))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N> javaslang.Function14<String, String, String, String, String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec14(Iso<TT, javaslang.Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10, JsonCodec<K> c11, JsonCodec<L> c12, JsonCodec<M> c13, JsonCodec<N> c14) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> c11.toJson(tuple._11).flatMap(j11 -> c12.toJson(tuple._12).flatMap(j12 -> c13.toJson(tuple._13).flatMap(j13 -> c14.toJson(tuple._14).flatMap(j14 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10), Json.entry(n11, j11), Json.entry(n12, j12), Json.entry(n13, j13), Json.entry(n14, j14)))))))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                Optional<K> ok = object.getAs(n11, c11::fromJson);
                Optional<L> ol = object.getAs(n12, c12::fromJson);
                Optional<M> om = object.getAs(n13, c13::fromJson);
                Optional<N> on = object.getAs(n14, c14::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> Optional.of(iso.reverseGet(new javaslang.Tuple14<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))))))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> javaslang.Function15<String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec15(Iso<TT, javaslang.Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10, JsonCodec<K> c11, JsonCodec<L> c12, JsonCodec<M> c13, JsonCodec<N> c14, JsonCodec<O> c15) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> c11.toJson(tuple._11).flatMap(j11 -> c12.toJson(tuple._12).flatMap(j12 -> c13.toJson(tuple._13).flatMap(j13 -> c14.toJson(tuple._14).flatMap(j14 -> c15.toJson(tuple._15).flatMap(j15 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10), Json.entry(n11, j11), Json.entry(n12, j12), Json.entry(n13, j13), Json.entry(n14, j14), Json.entry(n15, j15))))))))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                Optional<K> ok = object.getAs(n11, c11::fromJson);
                Optional<L> ol = object.getAs(n12, c12::fromJson);
                Optional<M> om = object.getAs(n13, c13::fromJson);
                Optional<N> on = object.getAs(n14, c14::fromJson);
                Optional<O> oo = object.getAs(n15, c15::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> Optional.of(iso.reverseGet(new javaslang.Tuple15<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o))))))))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> javaslang.Function16<String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec16(Iso<TT, javaslang.Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10, JsonCodec<K> c11, JsonCodec<L> c12, JsonCodec<M> c13, JsonCodec<N> c14, JsonCodec<O> c15, JsonCodec<P> c16) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> c11.toJson(tuple._11).flatMap(j11 -> c12.toJson(tuple._12).flatMap(j12 -> c13.toJson(tuple._13).flatMap(j13 -> c14.toJson(tuple._14).flatMap(j14 -> c15.toJson(tuple._15).flatMap(j15 -> c16.toJson(tuple._16).flatMap(j16 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10), Json.entry(n11, j11), Json.entry(n12, j12), Json.entry(n13, j13), Json.entry(n14, j14), Json.entry(n15, j15), Json.entry(n16, j16)))))))))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                Optional<K> ok = object.getAs(n11, c11::fromJson);
                Optional<L> ol = object.getAs(n12, c12::fromJson);
                Optional<M> om = object.getAs(n13, c13::fromJson);
                Optional<N> on = object.getAs(n14, c14::fromJson);
                Optional<O> oo = object.getAs(n15, c15::fromJson);
                Optional<P> op = object.getAs(n16, c16::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> Optional.of(iso.reverseGet(new javaslang.Tuple16<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))))))))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> javaslang.Function17<String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec17(Iso<TT, javaslang.Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10, JsonCodec<K> c11, JsonCodec<L> c12, JsonCodec<M> c13, JsonCodec<N> c14, JsonCodec<O> c15, JsonCodec<P> c16, JsonCodec<Q> c17) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16, n17) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> c11.toJson(tuple._11).flatMap(j11 -> c12.toJson(tuple._12).flatMap(j12 -> c13.toJson(tuple._13).flatMap(j13 -> c14.toJson(tuple._14).flatMap(j14 -> c15.toJson(tuple._15).flatMap(j15 -> c16.toJson(tuple._16).flatMap(j16 -> c17.toJson(tuple._17).flatMap(j17 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10), Json.entry(n11, j11), Json.entry(n12, j12), Json.entry(n13, j13), Json.entry(n14, j14), Json.entry(n15, j15), Json.entry(n16, j16), Json.entry(n17, j17))))))))))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                Optional<K> ok = object.getAs(n11, c11::fromJson);
                Optional<L> ol = object.getAs(n12, c12::fromJson);
                Optional<M> om = object.getAs(n13, c13::fromJson);
                Optional<N> on = object.getAs(n14, c14::fromJson);
                Optional<O> oo = object.getAs(n15, c15::fromJson);
                Optional<P> op = object.getAs(n16, c16::fromJson);
                Optional<Q> oq = object.getAs(n17, c17::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> oq.flatMap(q -> Optional.of(iso.reverseGet(new javaslang.Tuple17<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q))))))))))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> javaslang.Function18<String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec18(Iso<TT, javaslang.Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10, JsonCodec<K> c11, JsonCodec<L> c12, JsonCodec<M> c13, JsonCodec<N> c14, JsonCodec<O> c15, JsonCodec<P> c16, JsonCodec<Q> c17, JsonCodec<R> c18) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16, n17, n18) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> c11.toJson(tuple._11).flatMap(j11 -> c12.toJson(tuple._12).flatMap(j12 -> c13.toJson(tuple._13).flatMap(j13 -> c14.toJson(tuple._14).flatMap(j14 -> c15.toJson(tuple._15).flatMap(j15 -> c16.toJson(tuple._16).flatMap(j16 -> c17.toJson(tuple._17).flatMap(j17 -> c18.toJson(tuple._18).flatMap(j18 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10), Json.entry(n11, j11), Json.entry(n12, j12), Json.entry(n13, j13), Json.entry(n14, j14), Json.entry(n15, j15), Json.entry(n16, j16), Json.entry(n17, j17), Json.entry(n18, j18)))))))))))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                Optional<K> ok = object.getAs(n11, c11::fromJson);
                Optional<L> ol = object.getAs(n12, c12::fromJson);
                Optional<M> om = object.getAs(n13, c13::fromJson);
                Optional<N> on = object.getAs(n14, c14::fromJson);
                Optional<O> oo = object.getAs(n15, c15::fromJson);
                Optional<P> op = object.getAs(n16, c16::fromJson);
                Optional<Q> oq = object.getAs(n17, c17::fromJson);
                Optional<R> or = object.getAs(n18, c18::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> oq.flatMap(q -> or.flatMap(r -> Optional.of(iso.reverseGet(new javaslang.Tuple18<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))))))))))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> javaslang.Function19<String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec19(Iso<TT, javaslang.Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10, JsonCodec<K> c11, JsonCodec<L> c12, JsonCodec<M> c13, JsonCodec<N> c14, JsonCodec<O> c15, JsonCodec<P> c16, JsonCodec<Q> c17, JsonCodec<R> c18, JsonCodec<S> c19) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16, n17, n18, n19) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> c11.toJson(tuple._11).flatMap(j11 -> c12.toJson(tuple._12).flatMap(j12 -> c13.toJson(tuple._13).flatMap(j13 -> c14.toJson(tuple._14).flatMap(j14 -> c15.toJson(tuple._15).flatMap(j15 -> c16.toJson(tuple._16).flatMap(j16 -> c17.toJson(tuple._17).flatMap(j17 -> c18.toJson(tuple._18).flatMap(j18 -> c19.toJson(tuple._19).flatMap(j19 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10), Json.entry(n11, j11), Json.entry(n12, j12), Json.entry(n13, j13), Json.entry(n14, j14), Json.entry(n15, j15), Json.entry(n16, j16), Json.entry(n17, j17), Json.entry(n18, j18), Json.entry(n19, j19))))))))))))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                Optional<K> ok = object.getAs(n11, c11::fromJson);
                Optional<L> ol = object.getAs(n12, c12::fromJson);
                Optional<M> om = object.getAs(n13, c13::fromJson);
                Optional<N> on = object.getAs(n14, c14::fromJson);
                Optional<O> oo = object.getAs(n15, c15::fromJson);
                Optional<P> op = object.getAs(n16, c16::fromJson);
                Optional<Q> oq = object.getAs(n17, c17::fromJson);
                Optional<R> or = object.getAs(n18, c18::fromJson);
                Optional<S> os = object.getAs(n19, c19::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> oq.flatMap(q -> or.flatMap(r -> os.flatMap(s -> Optional.of(iso.reverseGet(new javaslang.Tuple19<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s))))))))))))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> javaslang.Function20<String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec20(Iso<TT, javaslang.Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10, JsonCodec<K> c11, JsonCodec<L> c12, JsonCodec<M> c13, JsonCodec<N> c14, JsonCodec<O> c15, JsonCodec<P> c16, JsonCodec<Q> c17, JsonCodec<R> c18, JsonCodec<S> c19, JsonCodec<T> c20) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16, n17, n18, n19, n20) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> c11.toJson(tuple._11).flatMap(j11 -> c12.toJson(tuple._12).flatMap(j12 -> c13.toJson(tuple._13).flatMap(j13 -> c14.toJson(tuple._14).flatMap(j14 -> c15.toJson(tuple._15).flatMap(j15 -> c16.toJson(tuple._16).flatMap(j16 -> c17.toJson(tuple._17).flatMap(j17 -> c18.toJson(tuple._18).flatMap(j18 -> c19.toJson(tuple._19).flatMap(j19 -> c20.toJson(tuple._20).flatMap(j20 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10), Json.entry(n11, j11), Json.entry(n12, j12), Json.entry(n13, j13), Json.entry(n14, j14), Json.entry(n15, j15), Json.entry(n16, j16), Json.entry(n17, j17), Json.entry(n18, j18), Json.entry(n19, j19), Json.entry(n20, j20)))))))))))))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                Optional<K> ok = object.getAs(n11, c11::fromJson);
                Optional<L> ol = object.getAs(n12, c12::fromJson);
                Optional<M> om = object.getAs(n13, c13::fromJson);
                Optional<N> on = object.getAs(n14, c14::fromJson);
                Optional<O> oo = object.getAs(n15, c15::fromJson);
                Optional<P> op = object.getAs(n16, c16::fromJson);
                Optional<Q> oq = object.getAs(n17, c17::fromJson);
                Optional<R> or = object.getAs(n18, c18::fromJson);
                Optional<S> os = object.getAs(n19, c19::fromJson);
                Optional<T> ot = object.getAs(n20, c20::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> oq.flatMap(q -> or.flatMap(r -> os.flatMap(s -> ot.flatMap(t -> Optional.of(iso.reverseGet(new javaslang.Tuple20<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))))))))))))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> javaslang.Function21<String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec21(Iso<TT, javaslang.Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10, JsonCodec<K> c11, JsonCodec<L> c12, JsonCodec<M> c13, JsonCodec<N> c14, JsonCodec<O> c15, JsonCodec<P> c16, JsonCodec<Q> c17, JsonCodec<R> c18, JsonCodec<S> c19, JsonCodec<T> c20, JsonCodec<U> c21) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16, n17, n18, n19, n20, n21) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> c11.toJson(tuple._11).flatMap(j11 -> c12.toJson(tuple._12).flatMap(j12 -> c13.toJson(tuple._13).flatMap(j13 -> c14.toJson(tuple._14).flatMap(j14 -> c15.toJson(tuple._15).flatMap(j15 -> c16.toJson(tuple._16).flatMap(j16 -> c17.toJson(tuple._17).flatMap(j17 -> c18.toJson(tuple._18).flatMap(j18 -> c19.toJson(tuple._19).flatMap(j19 -> c20.toJson(tuple._20).flatMap(j20 -> c21.toJson(tuple._21).flatMap(j21 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10), Json.entry(n11, j11), Json.entry(n12, j12), Json.entry(n13, j13), Json.entry(n14, j14), Json.entry(n15, j15), Json.entry(n16, j16), Json.entry(n17, j17), Json.entry(n18, j18), Json.entry(n19, j19), Json.entry(n20, j20), Json.entry(n21, j21))))))))))))))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                Optional<K> ok = object.getAs(n11, c11::fromJson);
                Optional<L> ol = object.getAs(n12, c12::fromJson);
                Optional<M> om = object.getAs(n13, c13::fromJson);
                Optional<N> on = object.getAs(n14, c14::fromJson);
                Optional<O> oo = object.getAs(n15, c15::fromJson);
                Optional<P> op = object.getAs(n16, c16::fromJson);
                Optional<Q> oq = object.getAs(n17, c17::fromJson);
                Optional<R> or = object.getAs(n18, c18::fromJson);
                Optional<S> os = object.getAs(n19, c19::fromJson);
                Optional<T> ot = object.getAs(n20, c20::fromJson);
                Optional<U> ou = object.getAs(n21, c21::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> oq.flatMap(q -> or.flatMap(r -> os.flatMap(s -> ot.flatMap(t -> ou.flatMap(u -> Optional.of(iso.reverseGet(new javaslang.Tuple21<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u))))))))))))))))))))))));
            }
        };
    }


    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> javaslang.Function22<String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, JsonCodec<TT>> codec22(Iso<TT, javaslang.Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8, JsonCodec<I> c9, JsonCodec<J> c10, JsonCodec<K> c11, JsonCodec<L> c12, JsonCodec<M> c13, JsonCodec<N> c14, JsonCodec<O> c15, JsonCodec<P> c16, JsonCodec<Q> c17, JsonCodec<R> c18, JsonCodec<S> c19, JsonCodec<T> c20, JsonCodec<U> c21, JsonCodec<V> c22) {
        return (n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16, n17, n18, n19, n20, n21, n22) -> new JsonCodec<TT>() {
            @Override
            public Optional<Json.JValue> toJson(TT value) {
                javaslang.Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> c9.toJson(tuple._9).flatMap(j9 -> c10.toJson(tuple._10).flatMap(j10 -> c11.toJson(tuple._11).flatMap(j11 -> c12.toJson(tuple._12).flatMap(j12 -> c13.toJson(tuple._13).flatMap(j13 -> c14.toJson(tuple._14).flatMap(j14 -> c15.toJson(tuple._15).flatMap(j15 -> c16.toJson(tuple._16).flatMap(j16 -> c17.toJson(tuple._17).flatMap(j17 -> c18.toJson(tuple._18).flatMap(j18 -> c19.toJson(tuple._19).flatMap(j19 -> c20.toJson(tuple._20).flatMap(j20 -> c21.toJson(tuple._21).flatMap(j21 -> c22.toJson(tuple._22).flatMap(j22 -> Optional.of(Json.jObject(Json.entry(n1, j1), Json.entry(n2, j2), Json.entry(n3, j3), Json.entry(n4, j4), Json.entry(n5, j5), Json.entry(n6, j6), Json.entry(n7, j7), Json.entry(n8, j8), Json.entry(n9, j9), Json.entry(n10, j10), Json.entry(n11, j11), Json.entry(n12, j12), Json.entry(n13, j13), Json.entry(n14, j14), Json.entry(n15, j15), Json.entry(n16, j16), Json.entry(n17, j17), Json.entry(n18, j18), Json.entry(n19, j19), Json.entry(n20, j20), Json.entry(n21, j21), Json.entry(n22, j22)))))))))))))))))))))))));
            }

            @Override
            public Optional<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Optional<A> oa = object.getAs(n1, c1::fromJson);
                Optional<B> ob = object.getAs(n2, c2::fromJson);
                Optional<C> oc = object.getAs(n3, c3::fromJson);
                Optional<D> od = object.getAs(n4, c4::fromJson);
                Optional<E> oe = object.getAs(n5, c5::fromJson);
                Optional<F> of = object.getAs(n6, c6::fromJson);
                Optional<G> og = object.getAs(n7, c7::fromJson);
                Optional<H> oh = object.getAs(n8, c8::fromJson);
                Optional<I> oi = object.getAs(n9, c9::fromJson);
                Optional<J> oj = object.getAs(n10, c10::fromJson);
                Optional<K> ok = object.getAs(n11, c11::fromJson);
                Optional<L> ol = object.getAs(n12, c12::fromJson);
                Optional<M> om = object.getAs(n13, c13::fromJson);
                Optional<N> on = object.getAs(n14, c14::fromJson);
                Optional<O> oo = object.getAs(n15, c15::fromJson);
                Optional<P> op = object.getAs(n16, c16::fromJson);
                Optional<Q> oq = object.getAs(n17, c17::fromJson);
                Optional<R> or = object.getAs(n18, c18::fromJson);
                Optional<S> os = object.getAs(n19, c19::fromJson);
                Optional<T> ot = object.getAs(n20, c20::fromJson);
                Optional<U> ou = object.getAs(n21, c21::fromJson);
                Optional<V> ov = object.getAs(n22, c22::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> oq.flatMap(q -> or.flatMap(r -> os.flatMap(s -> ot.flatMap(t -> ou.flatMap(u -> ov.flatMap(v -> Optional.of(iso.reverseGet(new javaslang.Tuple22<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))))))))))))))))))))))));
            }
        };
    }
}
