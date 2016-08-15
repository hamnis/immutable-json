package net.hamnaberg.json;

public interface Iso<A, B> {
    A reverseGet(B b);
    B get(A a);

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
}
