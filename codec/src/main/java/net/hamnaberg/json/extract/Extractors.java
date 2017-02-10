package net.hamnaberg.json.extract;

import javaslang.*;
import net.hamnaberg.json.codec.DecodeResult;
import net.hamnaberg.json.util.*;


public abstract class Extractors {
    private Extractors() {
    }

    public static <TT, A> Extractor<TT> extract(TypedField<A> f1, Function1<A, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            return oa.flatMap(a -> DecodeResult.ok(func.apply(a)));
        };
    }


    public static <TT, A, B> Extractor<TT> extract(TypedField<A> f1, TypedField<B> f2, Function2<A, B, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            DecodeResult<B> ob = DecodeResult.decode(object, f2.name, f2.decoder);
            return oa.flatMap(a -> ob.flatMap(b -> DecodeResult.ok(func.apply(a, b))));
        };
    }


    public static <TT, A, B, C> Extractor<TT> extract(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, Function3<A, B, C, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            DecodeResult<B> ob = DecodeResult.decode(object, f2.name, f2.decoder);
            DecodeResult<C> oc = DecodeResult.decode(object, f3.name, f3.decoder);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> DecodeResult.ok(func.apply(a, b, c)))));
        };
    }


    public static <TT, A, B, C, D> Extractor<TT> extract(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, Function4<A, B, C, D, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            DecodeResult<B> ob = DecodeResult.decode(object, f2.name, f2.decoder);
            DecodeResult<C> oc = DecodeResult.decode(object, f3.name, f3.decoder);
            DecodeResult<D> od = DecodeResult.decode(object, f4.name, f4.decoder);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> DecodeResult.ok(func.apply(a, b, c, d))))));
        };
    }


    public static <TT, A, B, C, D, E> Extractor<TT> extract(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, Function5<A, B, C, D, E, TT> func) {
        return (object) -> {
            DecodeResult<A> oa = DecodeResult.decode(object, f1.name, f1.decoder);
            DecodeResult<B> ob = DecodeResult.decode(object, f2.name, f2.decoder);
            DecodeResult<C> oc = DecodeResult.decode(object, f3.name, f3.decoder);
            DecodeResult<D> od = DecodeResult.decode(object, f4.name, f4.decoder);
            DecodeResult<E> oe = DecodeResult.decode(object, f5.name, f5.decoder);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> DecodeResult.ok(func.apply(a, b, c, d, e)))))));
        };
    }


    public static <TT, A, B, C, D, E, F> Extractor<TT> extract(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, Function6<A, B, C, D, E, F, TT> func) {
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


    public static <TT, A, B, C, D, E, F, G> Extractor<TT> extract(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, Function7<A, B, C, D, E, F, G, TT> func) {
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


    public static <TT, A, B, C, D, E, F, G, H> Extractor<TT> extract(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, Function8<A, B, C, D, E, F, G, H, TT> func) {
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

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, F9<A1, A2, A3, A4, A5, A6, A7, A8, A9, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9)))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, F10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, F11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, F12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, F13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, F14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, F15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, F16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, tf16.name, tf16.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, F17<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, tf16.name, tf16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, tf17.name, tf17.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, F18<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, tf16.name, tf16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, tf17.name, tf17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, tf18.name, tf18.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, F19<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, tf16.name, tf16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, tf17.name, tf17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, tf18.name, tf18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, tf19.name, tf19.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, F20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, tf16.name, tf16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, tf17.name, tf17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, tf18.name, tf18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, tf19.name, tf19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, tf20.name, tf20.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, F21<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, tf16.name, tf16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, tf17.name, tf17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, tf18.name, tf18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, tf19.name, tf19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, tf20.name, tf20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, tf21.name, tf21.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, TypedField<A22> tf22, F22<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, tf16.name, tf16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, tf17.name, tf17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, tf18.name, tf18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, tf19.name, tf19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, tf20.name, tf20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, tf21.name, tf21.decoder);
            DecodeResult<A22> d22 = DecodeResult.decode(object, tf22.name, tf22.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, TypedField<A22> tf22, TypedField<A23> tf23, F23<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, tf16.name, tf16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, tf17.name, tf17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, tf18.name, tf18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, tf19.name, tf19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, tf20.name, tf20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, tf21.name, tf21.decoder);
            DecodeResult<A22> d22 = DecodeResult.decode(object, tf22.name, tf22.decoder);
            DecodeResult<A23> d23 = DecodeResult.decode(object, tf23.name, tf23.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23)))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, TypedField<A22> tf22, TypedField<A23> tf23, TypedField<A24> tf24, F24<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, tf16.name, tf16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, tf17.name, tf17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, tf18.name, tf18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, tf19.name, tf19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, tf20.name, tf20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, tf21.name, tf21.decoder);
            DecodeResult<A22> d22 = DecodeResult.decode(object, tf22.name, tf22.decoder);
            DecodeResult<A23> d23 = DecodeResult.decode(object, tf23.name, tf23.decoder);
            DecodeResult<A24> d24 = DecodeResult.decode(object, tf24.name, tf24.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24))))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, TypedField<A22> tf22, TypedField<A23> tf23, TypedField<A24> tf24, TypedField<A25> tf25, F25<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, tf16.name, tf16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, tf17.name, tf17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, tf18.name, tf18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, tf19.name, tf19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, tf20.name, tf20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, tf21.name, tf21.decoder);
            DecodeResult<A22> d22 = DecodeResult.decode(object, tf22.name, tf22.decoder);
            DecodeResult<A23> d23 = DecodeResult.decode(object, tf23.name, tf23.decoder);
            DecodeResult<A24> d24 = DecodeResult.decode(object, tf24.name, tf24.decoder);
            DecodeResult<A25> d25 = DecodeResult.decode(object, tf25.name, tf25.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 -> d25.flatMap(v25 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25)))))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, TypedField<A22> tf22, TypedField<A23> tf23, TypedField<A24> tf24, TypedField<A25> tf25, TypedField<A26> tf26, F26<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, tf16.name, tf16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, tf17.name, tf17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, tf18.name, tf18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, tf19.name, tf19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, tf20.name, tf20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, tf21.name, tf21.decoder);
            DecodeResult<A22> d22 = DecodeResult.decode(object, tf22.name, tf22.decoder);
            DecodeResult<A23> d23 = DecodeResult.decode(object, tf23.name, tf23.decoder);
            DecodeResult<A24> d24 = DecodeResult.decode(object, tf24.name, tf24.decoder);
            DecodeResult<A25> d25 = DecodeResult.decode(object, tf25.name, tf25.decoder);
            DecodeResult<A26> d26 = DecodeResult.decode(object, tf26.name, tf26.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 -> d25.flatMap(v25 -> d26.flatMap(v26 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26))))))))))))))))))))))))))));
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, TypedField<A22> tf22, TypedField<A23> tf23, TypedField<A24> tf24, TypedField<A25> tf25, TypedField<A26> tf26, TypedField<A27> tf27, F27<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27, TT> func) {
        return (object) -> {
            DecodeResult<A1> d1 = DecodeResult.decode(object, tf1.name, tf1.decoder);
            DecodeResult<A2> d2 = DecodeResult.decode(object, tf2.name, tf2.decoder);
            DecodeResult<A3> d3 = DecodeResult.decode(object, tf3.name, tf3.decoder);
            DecodeResult<A4> d4 = DecodeResult.decode(object, tf4.name, tf4.decoder);
            DecodeResult<A5> d5 = DecodeResult.decode(object, tf5.name, tf5.decoder);
            DecodeResult<A6> d6 = DecodeResult.decode(object, tf6.name, tf6.decoder);
            DecodeResult<A7> d7 = DecodeResult.decode(object, tf7.name, tf7.decoder);
            DecodeResult<A8> d8 = DecodeResult.decode(object, tf8.name, tf8.decoder);
            DecodeResult<A9> d9 = DecodeResult.decode(object, tf9.name, tf9.decoder);
            DecodeResult<A10> d10 = DecodeResult.decode(object, tf10.name, tf10.decoder);
            DecodeResult<A11> d11 = DecodeResult.decode(object, tf11.name, tf11.decoder);
            DecodeResult<A12> d12 = DecodeResult.decode(object, tf12.name, tf12.decoder);
            DecodeResult<A13> d13 = DecodeResult.decode(object, tf13.name, tf13.decoder);
            DecodeResult<A14> d14 = DecodeResult.decode(object, tf14.name, tf14.decoder);
            DecodeResult<A15> d15 = DecodeResult.decode(object, tf15.name, tf15.decoder);
            DecodeResult<A16> d16 = DecodeResult.decode(object, tf16.name, tf16.decoder);
            DecodeResult<A17> d17 = DecodeResult.decode(object, tf17.name, tf17.decoder);
            DecodeResult<A18> d18 = DecodeResult.decode(object, tf18.name, tf18.decoder);
            DecodeResult<A19> d19 = DecodeResult.decode(object, tf19.name, tf19.decoder);
            DecodeResult<A20> d20 = DecodeResult.decode(object, tf20.name, tf20.decoder);
            DecodeResult<A21> d21 = DecodeResult.decode(object, tf21.name, tf21.decoder);
            DecodeResult<A22> d22 = DecodeResult.decode(object, tf22.name, tf22.decoder);
            DecodeResult<A23> d23 = DecodeResult.decode(object, tf23.name, tf23.decoder);
            DecodeResult<A24> d24 = DecodeResult.decode(object, tf24.name, tf24.decoder);
            DecodeResult<A25> d25 = DecodeResult.decode(object, tf25.name, tf25.decoder);
            DecodeResult<A26> d26 = DecodeResult.decode(object, tf26.name, tf26.decoder);
            DecodeResult<A27> d27 = DecodeResult.decode(object, tf27.name, tf27.decoder);
            return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 -> d25.flatMap(v25 -> d26.flatMap(v26 -> d27.flatMap(v27 ->  DecodeResult.ok(func.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27)))))))))))))))))))))))))))));
        };
    }
}
