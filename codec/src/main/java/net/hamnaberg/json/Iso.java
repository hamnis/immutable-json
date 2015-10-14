package net.hamnaberg.json;

import java.util.function.Function;

public interface Iso<A, B> {
    Function<B, A> reverseGet();
    Function<A, B> get();
}
