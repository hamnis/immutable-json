package net.hamnaberg.json.codec;

import java.util.function.Function;

public interface Iso<A, B> {
    A reverseGet(B b);
    B get(A a);

    default Function<A, A> modify(Function<B, B> f) {
        return a -> this.reverseGet(f.apply(this.get(a)));
    }

    default Iso<B, A> reverse() {
        Iso<A, B> that = this;
        return new Iso<B, A>() {
            @Override
            public B reverseGet(A a) {
                return that.get(a);
            }

            @Override
            public A get(B b) {
                return that.reverseGet(b);
            }
        };
    }

    default <C> Iso<A, C> compose(Iso<B, C> iso) {
        Iso<A, B> that = this;
        return new Iso<A, C>() {
            @Override
            public A reverseGet(C c) {
                return that.reverseGet(iso.reverseGet(c));
            }

            @Override
            public C get(A a) {
                return iso.get(that.get(a));
            }
        };
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
