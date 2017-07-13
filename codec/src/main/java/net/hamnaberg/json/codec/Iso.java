package net.hamnaberg.json.codec;

import java.util.function.Function;

public interface Iso<A, B> {
    A reverseGet(B b);
    B get(A a);

    default Function<A, A> modify(Function<B, B> f) {
        return a -> this.reverseGet(f.apply(this.get(a)));
    }

    default Iso<B, A> reverse() {
        return from(this::reverseGet, this::get);
    }

    default <C> Iso<A, C> compose(Iso<B, C> iso) {
        Function<B, A> reverseGet = this::reverseGet;
        Function<A, B> get = this::get;

        return from(get.andThen(iso::get), reverseGet.compose(iso::reverseGet));
    }

    static <A> Iso<A, A> identity() {
        return new IdIso<>();
    }

    static <A, B> Iso<A, B> from(Function<A, B> getF, Function<B, A> reverseGetF) {
        return new Iso<A, B>() {
            @Override
            public A reverseGet(B b) {
                return reverseGetF.apply(b);
            }

            @Override
            public B get(A a) {
                return getF.apply(a);
            }
        };
    }
}
