package net.hamnaberg.json.codec;

public final class IdIso<A> implements Iso<A, A> {
    @Override
    public A reverseGet(A a) {
        return a;
    }

    @Override
    public A get(A a) {
        return a;
    }

    @Override
    public String toString() {
        return "IdentityIso";
    }
}
