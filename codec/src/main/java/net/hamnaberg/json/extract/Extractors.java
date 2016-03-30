package net.hamnaberg.json.extract;

import javaslang.*;
import javaslang.control.Option;

public abstract class Extractors {
    private Extractors() {
    }


    public static <TT, A> Extractor<TT> extract1(TypedField<A> f1, Function1<A, TT> func) {
        return (object) -> {
            Option<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            return oa.flatMap(a -> Option.of(func.apply(a)));
        };
    }


    public static <TT, A, B> Extractor<TT> extract2(TypedField<A> f1, TypedField<B> f2, Function2<A, B, TT> func) {
        return (object) -> {
            Option<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Option<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> Option.of(func.apply(a, b))));
        };
    }


    public static <TT, A, B, C> Extractor<TT> extract3(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, Function3<A, B, C, TT> func) {
        return (object) -> {
            Option<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Option<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Option<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> Option.of(func.apply(a, b, c)))));
        };
    }


    public static <TT, A, B, C, D> Extractor<TT> extract4(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, Function4<A, B, C, D, TT> func) {
        return (object) -> {
            Option<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Option<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Option<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Option<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> Option.of(func.apply(a, b, c, d))))));
        };
    }


    public static <TT, A, B, C, D, E> Extractor<TT> extract5(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, Function5<A, B, C, D, E, TT> func) {
        return (object) -> {
            Option<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Option<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Option<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Option<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Option<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> Option.of(func.apply(a, b, c, d, e)))))));
        };
    }


    public static <TT, A, B, C, D, E, F> Extractor<TT> extract6(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, Function6<A, B, C, D, E, F, TT> func) {
        return (object) -> {
            Option<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Option<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Option<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Option<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Option<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Option<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> Option.of(func.apply(a, b, c, d, e, f))))))));
        };
    }


    public static <TT, A, B, C, D, E, F, G> Extractor<TT> extract7(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, Function7<A, B, C, D, E, F, G, TT> func) {
        return (object) -> {
            Option<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Option<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Option<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Option<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Option<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Option<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Option<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> Option.of(func.apply(a, b, c, d, e, f, g)))))))));
        };
    }


    public static <TT, A, B, C, D, E, F, G, H> Extractor<TT> extract8(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, Function8<A, B, C, D, E, F, G, H, TT> func) {
        return (object) -> {
            Option<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Option<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Option<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Option<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Option<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Option<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Option<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Option<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> Option.of(func.apply(a, b, c, d, e, f, g, h))))))))));
        };
    }
}
