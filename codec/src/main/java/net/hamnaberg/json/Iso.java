package net.hamnaberg.json;

import javaslang.*;

public interface Iso<A, B> {
    A reverseGet(B b);
    B get(A a);


    static <A> Iso<A, A> iso1() {
        return new IdIso<>();
    }

    static <A, B> Iso<Tuple2<A, B>, Tuple2<A, B>> iso2() {
        return new IdIso<>();
    }

    static <A, B, C> Iso<Tuple3<A, B, C>, Tuple3<A, B, C>> iso3() {
        return new IdIso<>();
    }

    static <A, B, C, D> Iso<Tuple4<A, B, C, D>, Tuple4<A, B, C, D>> iso4() {
        return new IdIso<>();
    }

    static <A, B, C, D, E> Iso<Tuple5<A, B, C, D, E>, Tuple5<A, B, C, D, E>> iso5() {
        return new IdIso<>();
    }

    static <A, B, C, D, E, F> Iso<Tuple6<A, B, C, D, E, F>, Tuple6<A, B, C, D, E, F>> iso6() {
        return new IdIso<>();
    }

    static <A, B, C, D, E, F, G> Iso<Tuple7<A, B, C, D, E, F, G>, Tuple7<A, B, C, D, E, F, G>> iso7() {
        return new IdIso<>();
    }

    static <A, B, C, D, E, F, G, H> Iso<Tuple8<A, B, C, D, E, F, G, H>, Tuple8<A, B, C, D, E, F, G, H>> iso7() {
        return new IdIso<>();
    }
}
