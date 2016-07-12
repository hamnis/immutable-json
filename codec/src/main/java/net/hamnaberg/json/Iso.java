package net.hamnaberg.json;

public interface Iso<A, B> {
    A reverseGet(B b);
    B get(A a);


    static <A> Iso<A, A> identity() {
        return new IdIso<>();
    }
}
