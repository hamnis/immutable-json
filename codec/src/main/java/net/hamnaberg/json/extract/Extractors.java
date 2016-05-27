package net.hamnaberg.json.extract;

import javaslang.*;
import net.hamnaberg.json.DecodeResult;


public abstract class Extractors {
    private Extractors() {
    }

    public static <TT, A> Extractor<TT> extract1(TypedField<A> f1, Function1<A, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            return oa.flatMap(a -> DecodeResult.ok(func.apply(a)));
        };
    }


    public static <TT, A, B> Extractor<TT> extract2(TypedField<A> f1, TypedField<B> f2, Function2<A, B, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            DecodeResult<B> ob = DecodeResult.decode(object, f2.name, f2.decoder);
            return oa.flatMap(a -> ob.flatMap(b -> DecodeResult.ok(func.apply(a, b))));
        };
    }


    public static <TT, A, B, C> Extractor<TT> extract3(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, Function3<A, B, C, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            DecodeResult<B> ob = DecodeResult.decode(object, f2.name, f2.decoder);
            DecodeResult<C> oc = DecodeResult.decode(object, f3.name, f3.decoder);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> DecodeResult.ok(func.apply(a, b, c)))));
        };
    }


    public static <TT, A, B, C, D> Extractor<TT> extract4(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, Function4<A, B, C, D, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            DecodeResult<B> ob = DecodeResult.decode(object, f2.name, f2.decoder);
            DecodeResult<C> oc = DecodeResult.decode(object, f3.name, f3.decoder);
            DecodeResult<D> od = DecodeResult.decode(object, f4.name, f4.decoder);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> DecodeResult.ok(func.apply(a, b, c, d))))));
        };
    }


    public static <TT, A, B, C, D, E> Extractor<TT> extract5(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, Function5<A, B, C, D, E, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            DecodeResult<B> ob = DecodeResult.decode(object, f2.name, f2.decoder);
            DecodeResult<C> oc = DecodeResult.decode(object, f3.name, f3.decoder);
            DecodeResult<D> od = DecodeResult.decode(object, f4.name, f4.decoder);
            DecodeResult<E> oe = DecodeResult.decode(object, f5.name, f5.decoder);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> DecodeResult.ok(func.apply(a, b, c, d, e)))))));
        };
    }


    public static <TT, A, B, C, D, E, F> Extractor<TT> extract6(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, Function6<A, B, C, D, E, F, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            DecodeResult<B> ob = DecodeResult.decode(object, f2.name, f2.decoder);
            DecodeResult<C> oc = DecodeResult.decode(object, f3.name, f3.decoder);
            DecodeResult<D> od = DecodeResult.decode(object, f4.name, f4.decoder);
            DecodeResult<E> oe = DecodeResult.decode(object, f5.name, f5.decoder);
            DecodeResult<F> of = DecodeResult.decode(object, f6.name, f6.decoder);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> DecodeResult.ok(func.apply(a, b, c, d, e, f))))))));
        };
    }


    public static <TT, A, B, C, D, E, F, G> Extractor<TT> extract7(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, Function7<A, B, C, D, E, F, G, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            DecodeResult<B> ob = DecodeResult.decode(object, f2.name, f2.decoder);
            DecodeResult<C> oc = DecodeResult.decode(object, f3.name, f3.decoder);
            DecodeResult<D> od = DecodeResult.decode(object, f4.name, f4.decoder);
            DecodeResult<E> oe = DecodeResult.decode(object, f5.name, f5.decoder);
            DecodeResult<F> of = DecodeResult.decode(object, f6.name, f6.decoder);
            DecodeResult<G> og = DecodeResult.decode(object, f7.name, f7.decoder);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> DecodeResult.ok(func.apply(a, b, c, d, e, f, g)))))))));
        };
    }


    public static <TT, A, B, C, D, E, F, G, H> Extractor<TT> extract8(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, Function8<A, B, C, D, E, F, G, H, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            DecodeResult<B> ob = DecodeResult.decode(object, f2.name, f2.decoder);
            DecodeResult<C> oc = DecodeResult.decode(object, f3.name, f3.decoder);
            DecodeResult<D> od = DecodeResult.decode(object, f4.name, f4.decoder);
            DecodeResult<E> oe = DecodeResult.decode(object, f5.name, f5.decoder);
            DecodeResult<F> of = DecodeResult.decode(object, f6.name, f6.decoder);
            DecodeResult<G> og = DecodeResult.decode(object, f7.name, f7.decoder);
            DecodeResult<H> oh = DecodeResult.decode(object, f8.name, f8.decoder);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> DecodeResult.ok(func.apply(a, b, c, d, e, f, g, h))))))))));
        };
    }
}
