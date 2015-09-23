package net.hamnaberg.json.io;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Try<A> {
    Try(){}

    public abstract boolean isSuccess();

    public boolean isFailure() {
        return !isSuccess();
    }

    public abstract Success<A> asSuccess();

    public abstract Failure<A> asFailure();

    public A getOrElse(Supplier<A> defaultValue) {
        return isSuccess() ? asSuccess().get() : defaultValue.get();
    }

    public <B> Try<B> map(Function<A, B> f) {
        return isSuccess() ? new Success<>(f.apply(asSuccess().get())) : (Try<B>) asFailure();
    }
}