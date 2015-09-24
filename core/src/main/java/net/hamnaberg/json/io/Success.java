package net.hamnaberg.json.io;

import java.util.Objects;

public final class Success<A> extends Try<A> {
    private A value;

    public Success(A value) {
        this.value = Objects.requireNonNull(value, "Value MAY NOT be null");
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public Success<A> asSuccess() {
        return this;
    }

    @Override
    public Failure<A> asFailure() {
        throw new IllegalStateException("Not a Failure");
    }

    public A get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Success<?> success = (Success<?>) o;

        return value.equals(success.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
