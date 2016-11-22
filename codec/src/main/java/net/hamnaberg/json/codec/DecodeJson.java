package net.hamnaberg.json.codec;

import javaslang.control.Option;
import javaslang.control.Try;
import net.hamnaberg.json.Json;

import java.util.function.Function;

public interface DecodeJson<A> {
    DecodeResult<A> fromJson(Json.JValue value);

    default Option<A> defaultValue() {
        return Option.none();
    }

    default A fromJsonUnsafe(Json.JValue value) {
        return fromJson(value).toOption().getOrElse((A)null);
    }

    default <B> DecodeJson<B> map(Function<A, B> f) {
        return (json) -> this.fromJson(json).map(f);
    }

    default <B> DecodeJson<B> tryMap(Function<A, Try<B>> f) {
        return (json) -> this.fromJson(json).map(f).
                flatMap(t -> t.isSuccess() ? DecodeResult.ok(t.get()) : DecodeResult.fail(t.failed().get().getMessage()));
    }

    default <B> DecodeJson<B> flatMap(Function<A, DecodeJson<B>> f) {
        return value -> {
            DecodeResult<A> result = this.fromJson(value);
            return result.flatMap(a -> f.apply(a).fromJson(value));
        };
    }

    default DecodeJson<A> withDefaultValue(A defaultValue) {
        if (this instanceof DecodeJsonWithDefault) {
            return new DecodeJsonWithDefault<>(((DecodeJsonWithDefault<A>)this).delegate, defaultValue);
        }
        return new DecodeJsonWithDefault<>(this, defaultValue);
    }

    class DecodeJsonWithDefault<A> implements DecodeJson<A> {
        final DecodeJson<A> delegate;
        private final A defaultValue;

        public DecodeJsonWithDefault(DecodeJson<A> delegate, A defaultValue) {
            this.delegate = delegate;
            this.defaultValue = defaultValue;
        }

        @Override
        public DecodeResult<A> fromJson(Json.JValue value) {
            DecodeResult<A> res = delegate.fromJson(value);
            if (res.isFailure()) {
                return DecodeResult.ok(defaultValue);
            }
            return res;
        }

        @Override
        public Option<A> defaultValue() {
            return Option.some(defaultValue);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + String.format(" {delegate=%s, default=%s}", delegate, defaultValue);
        }
    }
}
