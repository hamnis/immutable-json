package net.hamnaberg.json.extract;

import java.util.Optional;

public abstract class Extractors {
    private Extractors() {
    }


    public static <TT, A> Extractor<TT> extract1(TypedField<A> f1, javaslang.Function1<A, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            return oa.flatMap(a -> Optional.of(func.apply(a)));
        };
    }

    public static <TT, A, B> Extractor<TT> extract2(TypedField<A> f1, TypedField<B> f2, javaslang.Function2<A, B, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> Optional.of(func.apply(a, b))));
        };
    }

    public static <TT, A, B, C> Extractor<TT> extract3(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, javaslang.Function3<A, B, C, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> Optional.of(func.apply(a, b, c)))));
        };
    }

    public static <TT, A, B, C, D> Extractor<TT> extract4(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, javaslang.Function4<A, B, C, D, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> Optional.of(func.apply(a, b, c, d))))));
        };
    }

    public static <TT, A, B, C, D, E> Extractor<TT> extract5(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, javaslang.Function5<A, B, C, D, E, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> Optional.of(func.apply(a, b, c, d, e)))))));
        };
    }

    public static <TT, A, B, C, D, E, F> Extractor<TT> extract6(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, javaslang.Function6<A, B, C, D, E, F, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> Optional.of(func.apply(a, b, c, d, e, f))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G> Extractor<TT> extract7(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, javaslang.Function7<A, B, C, D, E, F, G, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> Optional.of(func.apply(a, b, c, d, e, f, g)))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H> Extractor<TT> extract8(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, javaslang.Function8<A, B, C, D, E, F, G, H, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> Optional.of(func.apply(a, b, c, d, e, f, g, h))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I> Extractor<TT> extract9(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, javaslang.Function9<A, B, C, D, E, F, G, H, I, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i)))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J> Extractor<TT> extract10(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, javaslang.Function10<A, B, C, D, E, F, G, H, I, J, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j))))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J, K> Extractor<TT> extract11(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, TypedField<K> f11, javaslang.Function11<A, B, C, D, E, F, G, H, I, J, K, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            Optional<K> ok = object.getAs(f11.name, f11.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j, k)))))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L> Extractor<TT> extract12(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, TypedField<K> f11, TypedField<L> f12, javaslang.Function12<A, B, C, D, E, F, G, H, I, J, K, L, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            Optional<K> ok = object.getAs(f11.name, f11.decoder::fromJson);
            Optional<L> ol = object.getAs(f12.name, f12.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j, k, l))))))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M> Extractor<TT> extract13(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, TypedField<K> f11, TypedField<L> f12, TypedField<M> f13, javaslang.Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            Optional<K> ok = object.getAs(f11.name, f11.decoder::fromJson);
            Optional<L> ol = object.getAs(f12.name, f12.decoder::fromJson);
            Optional<M> om = object.getAs(f13.name, f13.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m)))))))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N> Extractor<TT> extract14(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, TypedField<K> f11, TypedField<L> f12, TypedField<M> f13, TypedField<N> f14, javaslang.Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            Optional<K> ok = object.getAs(f11.name, f11.decoder::fromJson);
            Optional<L> ol = object.getAs(f12.name, f12.decoder::fromJson);
            Optional<M> om = object.getAs(f13.name, f13.decoder::fromJson);
            Optional<N> on = object.getAs(f14.name, f14.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n))))))))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> Extractor<TT> extract15(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, TypedField<K> f11, TypedField<L> f12, TypedField<M> f13, TypedField<N> f14, TypedField<O> f15, javaslang.Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            Optional<K> ok = object.getAs(f11.name, f11.decoder::fromJson);
            Optional<L> ol = object.getAs(f12.name, f12.decoder::fromJson);
            Optional<M> om = object.getAs(f13.name, f13.decoder::fromJson);
            Optional<N> on = object.getAs(f14.name, f14.decoder::fromJson);
            Optional<O> oo = object.getAs(f15.name, f15.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))))))))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> Extractor<TT> extract16(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, TypedField<K> f11, TypedField<L> f12, TypedField<M> f13, TypedField<N> f14, TypedField<O> f15, TypedField<P> f16, javaslang.Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            Optional<K> ok = object.getAs(f11.name, f11.decoder::fromJson);
            Optional<L> ol = object.getAs(f12.name, f12.decoder::fromJson);
            Optional<M> om = object.getAs(f13.name, f13.decoder::fromJson);
            Optional<N> on = object.getAs(f14.name, f14.decoder::fromJson);
            Optional<O> oo = object.getAs(f15.name, f15.decoder::fromJson);
            Optional<P> op = object.getAs(f16.name, f16.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p))))))))))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Extractor<TT> extract17(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, TypedField<K> f11, TypedField<L> f12, TypedField<M> f13, TypedField<N> f14, TypedField<O> f15, TypedField<P> f16, TypedField<Q> f17, javaslang.Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            Optional<K> ok = object.getAs(f11.name, f11.decoder::fromJson);
            Optional<L> ol = object.getAs(f12.name, f12.decoder::fromJson);
            Optional<M> om = object.getAs(f13.name, f13.decoder::fromJson);
            Optional<N> on = object.getAs(f14.name, f14.decoder::fromJson);
            Optional<O> oo = object.getAs(f15.name, f15.decoder::fromJson);
            Optional<P> op = object.getAs(f16.name, f16.decoder::fromJson);
            Optional<Q> oq = object.getAs(f17.name, f17.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> oq.flatMap(q -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))))))))))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Extractor<TT> extract18(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, TypedField<K> f11, TypedField<L> f12, TypedField<M> f13, TypedField<N> f14, TypedField<O> f15, TypedField<P> f16, TypedField<Q> f17, TypedField<R> f18, javaslang.Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            Optional<K> ok = object.getAs(f11.name, f11.decoder::fromJson);
            Optional<L> ol = object.getAs(f12.name, f12.decoder::fromJson);
            Optional<M> om = object.getAs(f13.name, f13.decoder::fromJson);
            Optional<N> on = object.getAs(f14.name, f14.decoder::fromJson);
            Optional<O> oo = object.getAs(f15.name, f15.decoder::fromJson);
            Optional<P> op = object.getAs(f16.name, f16.decoder::fromJson);
            Optional<Q> oq = object.getAs(f17.name, f17.decoder::fromJson);
            Optional<R> or = object.getAs(f18.name, f18.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> oq.flatMap(q -> or.flatMap(r -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r))))))))))))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Extractor<TT> extract19(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, TypedField<K> f11, TypedField<L> f12, TypedField<M> f13, TypedField<N> f14, TypedField<O> f15, TypedField<P> f16, TypedField<Q> f17, TypedField<R> f18, TypedField<S> f19, javaslang.Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            Optional<K> ok = object.getAs(f11.name, f11.decoder::fromJson);
            Optional<L> ol = object.getAs(f12.name, f12.decoder::fromJson);
            Optional<M> om = object.getAs(f13.name, f13.decoder::fromJson);
            Optional<N> on = object.getAs(f14.name, f14.decoder::fromJson);
            Optional<O> oo = object.getAs(f15.name, f15.decoder::fromJson);
            Optional<P> op = object.getAs(f16.name, f16.decoder::fromJson);
            Optional<Q> oq = object.getAs(f17.name, f17.decoder::fromJson);
            Optional<R> or = object.getAs(f18.name, f18.decoder::fromJson);
            Optional<S> os = object.getAs(f19.name, f19.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> oq.flatMap(q -> or.flatMap(r -> os.flatMap(s -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))))))))))))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Extractor<TT> extract20(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, TypedField<K> f11, TypedField<L> f12, TypedField<M> f13, TypedField<N> f14, TypedField<O> f15, TypedField<P> f16, TypedField<Q> f17, TypedField<R> f18, TypedField<S> f19, TypedField<T> f20, javaslang.Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            Optional<K> ok = object.getAs(f11.name, f11.decoder::fromJson);
            Optional<L> ol = object.getAs(f12.name, f12.decoder::fromJson);
            Optional<M> om = object.getAs(f13.name, f13.decoder::fromJson);
            Optional<N> on = object.getAs(f14.name, f14.decoder::fromJson);
            Optional<O> oo = object.getAs(f15.name, f15.decoder::fromJson);
            Optional<P> op = object.getAs(f16.name, f16.decoder::fromJson);
            Optional<Q> oq = object.getAs(f17.name, f17.decoder::fromJson);
            Optional<R> or = object.getAs(f18.name, f18.decoder::fromJson);
            Optional<S> os = object.getAs(f19.name, f19.decoder::fromJson);
            Optional<T> ot = object.getAs(f20.name, f20.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> oq.flatMap(q -> or.flatMap(r -> os.flatMap(s -> ot.flatMap(t -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t))))))))))))))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Extractor<TT> extract21(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, TypedField<K> f11, TypedField<L> f12, TypedField<M> f13, TypedField<N> f14, TypedField<O> f15, TypedField<P> f16, TypedField<Q> f17, TypedField<R> f18, TypedField<S> f19, TypedField<T> f20, TypedField<U> f21, javaslang.Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            Optional<K> ok = object.getAs(f11.name, f11.decoder::fromJson);
            Optional<L> ol = object.getAs(f12.name, f12.decoder::fromJson);
            Optional<M> om = object.getAs(f13.name, f13.decoder::fromJson);
            Optional<N> on = object.getAs(f14.name, f14.decoder::fromJson);
            Optional<O> oo = object.getAs(f15.name, f15.decoder::fromJson);
            Optional<P> op = object.getAs(f16.name, f16.decoder::fromJson);
            Optional<Q> oq = object.getAs(f17.name, f17.decoder::fromJson);
            Optional<R> or = object.getAs(f18.name, f18.decoder::fromJson);
            Optional<S> os = object.getAs(f19.name, f19.decoder::fromJson);
            Optional<T> ot = object.getAs(f20.name, f20.decoder::fromJson);
            Optional<U> ou = object.getAs(f21.name, f21.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> oq.flatMap(q -> or.flatMap(r -> os.flatMap(s -> ot.flatMap(t -> ou.flatMap(u -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))))))))))))))))))))));
        };
    }

    public static <TT, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Extractor<TT> extract22(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, TypedField<D> f4, TypedField<E> f5, TypedField<F> f6, TypedField<G> f7, TypedField<H> f8, TypedField<I> f9, TypedField<J> f10, TypedField<K> f11, TypedField<L> f12, TypedField<M> f13, TypedField<N> f14, TypedField<O> f15, TypedField<P> f16, TypedField<Q> f17, TypedField<R> f18, TypedField<S> f19, TypedField<T> f20, TypedField<U> f21, TypedField<V> f22, javaslang.Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            Optional<D> od = object.getAs(f4.name, f4.decoder::fromJson);
            Optional<E> oe = object.getAs(f5.name, f5.decoder::fromJson);
            Optional<F> of = object.getAs(f6.name, f6.decoder::fromJson);
            Optional<G> og = object.getAs(f7.name, f7.decoder::fromJson);
            Optional<H> oh = object.getAs(f8.name, f8.decoder::fromJson);
            Optional<I> oi = object.getAs(f9.name, f9.decoder::fromJson);
            Optional<J> oj = object.getAs(f10.name, f10.decoder::fromJson);
            Optional<K> ok = object.getAs(f11.name, f11.decoder::fromJson);
            Optional<L> ol = object.getAs(f12.name, f12.decoder::fromJson);
            Optional<M> om = object.getAs(f13.name, f13.decoder::fromJson);
            Optional<N> on = object.getAs(f14.name, f14.decoder::fromJson);
            Optional<O> oo = object.getAs(f15.name, f15.decoder::fromJson);
            Optional<P> op = object.getAs(f16.name, f16.decoder::fromJson);
            Optional<Q> oq = object.getAs(f17.name, f17.decoder::fromJson);
            Optional<R> or = object.getAs(f18.name, f18.decoder::fromJson);
            Optional<S> os = object.getAs(f19.name, f19.decoder::fromJson);
            Optional<T> ot = object.getAs(f20.name, f20.decoder::fromJson);
            Optional<U> ou = object.getAs(f21.name, f21.decoder::fromJson);
            Optional<V> ov = object.getAs(f22.name, f22.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> oi.flatMap(i -> oj.flatMap(j -> ok.flatMap(k -> ol.flatMap(l -> om.flatMap(m -> on.flatMap(n -> oo.flatMap(o -> op.flatMap(p -> oq.flatMap(q -> or.flatMap(r -> os.flatMap(s -> ot.flatMap(t -> ou.flatMap(u -> ov.flatMap(v -> Optional.of(func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v))))))))))))))))))))))));
        };
    }
}
