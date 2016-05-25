package net.hamnaberg.json;

import javaslang.*;
import javaslang.collection.List;
import javaslang.control.Option;

import java.util.Optional;


public abstract class Codecs {
    private Codecs(){}
    public static final JsonCodec<String> StringCodec = new JsonCodec<String>() {
        @Override
        public Option<Json.JValue> toJson(String value) {
            return Option.of(Json.jString(value));
        }

        @Override
        public Option<String> fromJson(Json.JValue value) {
            return value.asString();
        }
    };

    public static final JsonCodec<Number> numberCodec = new JsonCodec<Number>() {
        @Override
        public Option<Json.JValue> toJson(Number value) {
            return Option.of(Json.jNumber(value));
        }

        @Override
        public Option<Number> fromJson(Json.JValue value) {
            return value.asBigDecimal().map(v -> (Number) v);
        }
    };

    public static final JsonCodec<Long> longCodec = new JsonCodec<Long>() {
        @Override
        public Option<Json.JValue> toJson(Long value) {
            return Option.of(Json.jNumber(value));
        }

        @Override
        public Option<Long> fromJson(Json.JValue value) {
            return value.asJsonNumber().map(Json.JNumber::asLong);
        }
    };

    public static final JsonCodec<Double> doubleCodec = new JsonCodec<Double>() {
        @Override
        public Option<Json.JValue> toJson(Double value) {
            return Option.of(Json.jNumber(value));
        }

        @Override
        public Option<Double> fromJson(Json.JValue value) {
            return value.asJsonNumber().map(Json.JNumber::asDouble);
        }
    };

    public static final JsonCodec<Integer> intCodec = new JsonCodec<Integer>() {
        @Override
        public Option<Json.JValue> toJson(Integer value) {
            return Option.of(Json.jNumber(value));
        }

        @Override
        public Option<Integer> fromJson(Json.JValue value) {
            return value.asJsonNumber().map(Json.JNumber::asInt);
        }
    };

    public static final JsonCodec<Boolean> booleanCodec = new JsonCodec<Boolean>() {
        @Override
        public Option<Json.JValue> toJson(Boolean value) {
            return Option.of(Json.jBoolean(value));
        }

        @Override
        public Option<Boolean> fromJson(Json.JValue value) {
            return value.asBoolean();
        }
    };

    public static <A> JsonCodec<A> nullCodec() {
        return new JsonCodec<A>() {
            @Override
            public Option<Json.JValue> toJson(A value) {
                return Option.of(Json.jNull());
            }

            @Override
            public Option<A> fromJson(Json.JValue value) {
                return Option.none();
            }
        };
    }

    public static <A> JsonCodec<List<A>> listCodec(JsonCodec<A> codec) {
        return new JsonCodec<List<A>>() {
            @Override
            public Option<List<A>> fromJson(Json.JValue value) {
                return value.asJsonArray().map(j -> j.mapOpt(codec::fromJson));
            }

            @Override
            public Option<Json.JValue> toJson(List<A> value) {
                return Option.of(Json.jArray(value.flatMap(a -> codec.toJson(a).toList())));
            }
        };
    }

    public static <A> JsonCodec<java.util.List<A>> javaListCodec(JsonCodec<A> codec) {
        return listCodec(codec).xmap(List::toJavaList, List::ofAll);
    }

    public static <A> JsonCodec<Option<A>> OptionCodec(JsonCodec<A> codec) {
        return new JsonCodec<Option<A>>() {
            @Override
            public Option<Option<A>> fromJson(Json.JValue value) {
                return Option.of(codec.fromJson(value));
            }

            @Override
            public Option<Json.JValue> toJson(Option<A> value) {
                return value.flatMap(codec::toJson);
            }
        };
    }

    public static <A> JsonCodec<Optional<A>> OptionalCodec(JsonCodec<A> codec) {
        return OptionCodec(codec).xmap(Option::toJavaOptional, Option::ofOptional);
    }


    public static <TT, A> Function1<String, JsonCodec<TT>> codec1(Iso<TT, Tuple1<A>> iso, JsonCodec<A> c1) {
        return (n1) -> new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple1<A> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> Option.of(Json.jObject(Json.entry(n1, j1))));
            }

            @Override
            public Option<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Option<A> oa = object.getAs(n1, c1::fromJson);
                return oa.flatMap(a -> Option.of(iso.reverseGet(new Tuple1<>(a))));
            }
        };
    }



    public static <TT, A, B> Function2<String,String, JsonCodec<TT>> codec2(Iso<TT, Tuple2<A, B>> iso, JsonCodec<A> c1, JsonCodec<B> c2) {
        return (n1,n2) -> new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple2<A, B> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> Option.of(Json.jObject(Json.entry(n1, j1),Json.entry(n2, j2)))));
            }

            @Override
            public Option<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Option<A> oa = object.getAs(n1, c1::fromJson);
                Option<B> ob = object.getAs(n2, c2::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> Option.of(iso.reverseGet(new Tuple2<>(a,b)))));
            }
        };
    }



    public static <TT, A, B, C> Function3<String,String,String, JsonCodec<TT>> codec3(Iso<TT, Tuple3<A, B, C>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3) {
        return (n1,n2,n3) -> new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple3<A, B, C> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> Option.of(Json.jObject(Json.entry(n1, j1),Json.entry(n2, j2),Json.entry(n3, j3))))));
            }

            @Override
            public Option<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Option<A> oa = object.getAs(n1, c1::fromJson);
                Option<B> ob = object.getAs(n2, c2::fromJson);
                Option<C> oc = object.getAs(n3, c3::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> Option.of(iso.reverseGet(new Tuple3<>(a,b,c))))));
            }
        };
    }



    public static <TT, A, B, C, D> Function4<String,String,String,String, JsonCodec<TT>> codec4(Iso<TT, Tuple4<A, B, C, D>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4) {
        return (n1,n2,n3,n4) -> new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple4<A, B, C, D> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> Option.of(Json.jObject(Json.entry(n1, j1),Json.entry(n2, j2),Json.entry(n3, j3),Json.entry(n4, j4)))))));
            }

            @Override
            public Option<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Option<A> oa = object.getAs(n1, c1::fromJson);
                Option<B> ob = object.getAs(n2, c2::fromJson);
                Option<C> oc = object.getAs(n3, c3::fromJson);
                Option<D> od = object.getAs(n4, c4::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> Option.of(iso.reverseGet(new Tuple4<>(a,b,c,d)))))));
            }
        };
    }



    public static <TT, A, B, C, D, E> Function5<String,String,String,String,String, JsonCodec<TT>> codec5(Iso<TT, Tuple5<A, B, C, D, E>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5) {
        return (n1,n2,n3,n4,n5) -> new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple5<A, B, C, D, E> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> Option.of(Json.jObject(Json.entry(n1, j1),Json.entry(n2, j2),Json.entry(n3, j3),Json.entry(n4, j4),Json.entry(n5, j5))))))));
            }

            @Override
            public Option<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Option<A> oa = object.getAs(n1, c1::fromJson);
                Option<B> ob = object.getAs(n2, c2::fromJson);
                Option<C> oc = object.getAs(n3, c3::fromJson);
                Option<D> od = object.getAs(n4, c4::fromJson);
                Option<E> oe = object.getAs(n5, c5::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> Option.of(iso.reverseGet(new Tuple5<>(a,b,c,d,e))))))));
            }
        };
    }



    public static <TT, A, B, C, D, E, F> Function6<String,String,String,String,String,String, JsonCodec<TT>> codec6(Iso<TT, Tuple6<A, B, C, D, E, F>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6) {
        return (n1,n2,n3,n4,n5,n6) -> new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple6<A, B, C, D, E, F> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> Option.of(Json.jObject(Json.entry(n1, j1),Json.entry(n2, j2),Json.entry(n3, j3),Json.entry(n4, j4),Json.entry(n5, j5),Json.entry(n6, j6)))))))));
            }

            @Override
            public Option<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Option<A> oa = object.getAs(n1, c1::fromJson);
                Option<B> ob = object.getAs(n2, c2::fromJson);
                Option<C> oc = object.getAs(n3, c3::fromJson);
                Option<D> od = object.getAs(n4, c4::fromJson);
                Option<E> oe = object.getAs(n5, c5::fromJson);
                Option<F> of = object.getAs(n6, c6::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> Option.of(iso.reverseGet(new Tuple6<>(a,b,c,d,e,f)))))))));
            }
        };
    }



    public static <TT, A, B, C, D, E, F, G> Function7<String,String,String,String,String,String,String, JsonCodec<TT>> codec7(Iso<TT, Tuple7<A, B, C, D, E, F, G>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7) {
        return (n1,n2,n3,n4,n5,n6,n7) -> new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple7<A, B, C, D, E, F, G> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> Option.of(Json.jObject(Json.entry(n1, j1),Json.entry(n2, j2),Json.entry(n3, j3),Json.entry(n4, j4),Json.entry(n5, j5),Json.entry(n6, j6),Json.entry(n7, j7))))))))));
            }

            @Override
            public Option<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Option<A> oa = object.getAs(n1, c1::fromJson);
                Option<B> ob = object.getAs(n2, c2::fromJson);
                Option<C> oc = object.getAs(n3, c3::fromJson);
                Option<D> od = object.getAs(n4, c4::fromJson);
                Option<E> oe = object.getAs(n5, c5::fromJson);
                Option<F> of = object.getAs(n6, c6::fromJson);
                Option<G> og = object.getAs(n7, c7::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> Option.of(iso.reverseGet(new Tuple7<>(a,b,c,d,e,f,g))))))))));
            }
        };
    }



    public static <TT, A, B, C, D, E, F, G, H> Function8<String,String,String,String,String,String,String,String, JsonCodec<TT>> codec8(Iso<TT, Tuple8<A, B, C, D, E, F, G, H>> iso, JsonCodec<A> c1, JsonCodec<B> c2, JsonCodec<C> c3, JsonCodec<D> c4, JsonCodec<E> c5, JsonCodec<F> c6, JsonCodec<G> c7, JsonCodec<H> c8) {
        return (n1,n2,n3,n4,n5,n6,n7,n8) -> new JsonCodec<TT>() {
            @Override
            public Option<Json.JValue> toJson(TT value) {
                Tuple8<A, B, C, D, E, F, G, H> tuple = iso.get(value);
                return c1.toJson(tuple._1).flatMap(j1 -> c2.toJson(tuple._2).flatMap(j2 -> c3.toJson(tuple._3).flatMap(j3 -> c4.toJson(tuple._4).flatMap(j4 -> c5.toJson(tuple._5).flatMap(j5 -> c6.toJson(tuple._6).flatMap(j6 -> c7.toJson(tuple._7).flatMap(j7 -> c8.toJson(tuple._8).flatMap(j8 -> Option.of(Json.jObject(Json.entry(n1, j1),Json.entry(n2, j2),Json.entry(n3, j3),Json.entry(n4, j4),Json.entry(n5, j5),Json.entry(n6, j6),Json.entry(n7, j7),Json.entry(n8, j8)))))))))));
            }

            @Override
            public Option<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                Option<A> oa = object.getAs(n1, c1::fromJson);
                Option<B> ob = object.getAs(n2, c2::fromJson);
                Option<C> oc = object.getAs(n3, c3::fromJson);
                Option<D> od = object.getAs(n4, c4::fromJson);
                Option<E> oe = object.getAs(n5, c5::fromJson);
                Option<F> of = object.getAs(n6, c6::fromJson);
                Option<G> og = object.getAs(n7, c7::fromJson);
                Option<H> oh = object.getAs(n8, c8::fromJson);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> Option.of(iso.reverseGet(new Tuple8<>(a,b,c,d,e,f,g,h)))))))))));
            }
        };
    }
}
