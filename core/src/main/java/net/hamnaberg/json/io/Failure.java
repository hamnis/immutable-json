package net.hamnaberg.json.io;

import java.util.Objects;

public final class Failure<A> extends Try<A> {
    private final Exception exception;

    public Failure(Exception exception) {
        this.exception = Objects.requireNonNull(exception, "Exception may not be null");
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public Success<A> asSuccess() {
        throw new IllegalStateException("Not a Success");
    }

    @Override
    public Failure<A> asFailure() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Failure<?> failure = (Failure<?>) o;

        return exception.equals(failure.exception);

    }

    @Override
    public int hashCode() {
        return exception.hashCode();
    }
}
