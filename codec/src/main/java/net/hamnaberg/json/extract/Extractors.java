package net.hamnaberg.json.extract;

import javaslang.*;
import javaslang.control.Option;
import net.hamnaberg.json.DecodeResult;
import net.hamnaberg.json.Json;

import java.util.function.Function;

public abstract class Extractors {
    private Extractors() {
    }

    private static DecodeResult<Json.JValue> getValue(Json.JObject object, String name) {
        return object.
                get(name).
                map(DecodeResult::ok).
                getOrElse(DecodeResult.fail(String.format("%s not found in object", name)));
    }

    private static <A> DecodeResult<A> getValueAs(Json.JObject object, String name, Function<Json.JValue, DecodeResult<A>> f, Option<A> orDefault) {
        DecodeResult<A> result = getValue(object, name).flatMap(f);
        if (result.isFailure() && orDefault.isDefined()) {
            return DecodeResult.ok(orDefault.get());
        }
        return result;
    }

    public static <TT, A> Extractor<TT> extract1(TypedField<A> f1, Function1<A, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = getValueAs(object, f1.name, f1.decoder::fromJson, f1.defaultValue);
            return oa.flatMap(a -> DecodeResult.ok(func.apply(a)));
        };
    }


    public static <TT, A, B> Extractor<TT> extract2(TypedField<A> f1, TypedField<B> f2, Function2<A, B, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = getValueAs(object, f1.name, f1.decoder::fromJson, f1.defaultValue);
            DecodeResult<B> ob = getValueAs(object, f2.name, f2.decoder::fromJson, f2.defaultValue);
            return oa.flatMap(a -> ob.flatMap(b -> DecodeResult.ok(func.apply(a, b))));
        };
    }


    public static <TT, A, B, C> Extractor<TT> extract3(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, Function3<A, B, C, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = getValueAs(object, f1.name, f1.decoder::fromJson, f1.defaultValue);
            DecodeResult<B> ob = getValueAs(object, f2.name, f2.decoder::fromJson, f2.defaultValue);
            DecodeResult<C> oc = getValueAs(object, f3.name, f3.decoder::fromJson, f3.defaultValue);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> DecodeResult.ok(func.apply(a, b, c)))));
        };
    }


    public static <TT, A, B, C, D> Extractor<TT> extract4(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, Function4<A, B, C, D, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = getValueAs(object, f1.name, f1.decoder::fromJson, f1.defaultValue);
            DecodeResult<B> ob = getValueAs(object, f2.name, f2.decoder::fromJson, f2.defaultValue);
            DecodeResult<C> oc = getValueAs(object, f3.name, f3.decoder::fromJson, f3.defaultValue);
            DecodeResult<D> od = getValueAs(object, f4.name, f4.decoder::fromJson, f4.defaultValue);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> DecodeResult.ok(func.apply(a, b, c, d))))));
        };
    }


    public static <TT, A, B, C, D, E> Extractor<TT> extract5(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, Function5<A, B, C, D, E, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = getValueAs(object, f1.name, f1.decoder::fromJson, f1.defaultValue);
            DecodeResult<B> ob = getValueAs(object, f2.name, f2.decoder::fromJson, f2.defaultValue);
            DecodeResult<C> oc = getValueAs(object, f3.name, f3.decoder::fromJson, f3.defaultValue);
            DecodeResult<D> od = getValueAs(object, f4.name, f4.decoder::fromJson, f4.defaultValue);
            DecodeResult<E> oe = getValueAs(object, f5.name, f5.decoder::fromJson, f5.defaultValue);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> DecodeResult.ok(func.apply(a, b, c, d, e)))))));
        };
    }


    public static <TT, A, B, C, D, E, F> Extractor<TT> extract6(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, Function6<A, B, C, D, E, F, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = getValueAs(object, f1.name, f1.decoder::fromJson, f1.defaultValue);
            DecodeResult<B> ob = getValueAs(object, f2.name, f2.decoder::fromJson, f2.defaultValue);
            DecodeResult<C> oc = getValueAs(object, f3.name, f3.decoder::fromJson, f3.defaultValue);
            DecodeResult<D> od = getValueAs(object, f4.name, f4.decoder::fromJson, f4.defaultValue);
            DecodeResult<E> oe = getValueAs(object, f5.name, f5.decoder::fromJson, f5.defaultValue);
            DecodeResult<F> of = getValueAs(object, f6.name, f6.decoder::fromJson, f6.defaultValue);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> DecodeResult.ok(func.apply(a, b, c, d, e, f))))))));
        };
    }


    public static <TT, A, B, C, D, E, F, G> Extractor<TT> extract7(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, Function7<A, B, C, D, E, F, G, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = getValueAs(object, f1.name, f1.decoder::fromJson, f1.defaultValue);
            DecodeResult<B> ob = getValueAs(object, f2.name, f2.decoder::fromJson, f2.defaultValue);
            DecodeResult<C> oc = getValueAs(object, f3.name, f3.decoder::fromJson, f3.defaultValue);
            DecodeResult<D> od = getValueAs(object, f4.name, f4.decoder::fromJson, f4.defaultValue);
            DecodeResult<E> oe = getValueAs(object, f5.name, f5.decoder::fromJson, f5.defaultValue);
            DecodeResult<F> of = getValueAs(object, f6.name, f6.decoder::fromJson, f6.defaultValue);
            DecodeResult<G> og = getValueAs(object, f7.name, f7.decoder::fromJson, f7.defaultValue);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> DecodeResult.ok(func.apply(a, b, c, d, e, f, g)))))))));
        };
    }


    public static <TT, A, B, C, D, E, F, G, H> Extractor<TT> extract8(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, Function8<A, B, C, D, E, F, G, H, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = getValueAs(object, f1.name, f1.decoder::fromJson, f1.defaultValue);
            DecodeResult<B> ob = getValueAs(object, f2.name, f2.decoder::fromJson, f2.defaultValue);
            DecodeResult<C> oc = getValueAs(object, f3.name, f3.decoder::fromJson, f3.defaultValue);
            DecodeResult<D> od = getValueAs(object, f4.name, f4.decoder::fromJson, f4.defaultValue);
            DecodeResult<E> oe = getValueAs(object, f5.name, f5.decoder::fromJson, f5.defaultValue);
            DecodeResult<F> of = getValueAs(object, f6.name, f6.decoder::fromJson, f6.defaultValue);
            DecodeResult<G> og = getValueAs(object, f7.name, f7.decoder::fromJson, f7.defaultValue);
            DecodeResult<H> oh = getValueAs(object, f8.name, f8.decoder::fromJson, f8.defaultValue);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> DecodeResult.ok(func.apply(a, b, c, d, e, f, g, h))))))))));
        };
    }
}
