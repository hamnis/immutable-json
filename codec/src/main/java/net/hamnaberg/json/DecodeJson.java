package net.hamnaberg.json;

import javaslang.control.Option;

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

    default <B> DecodeJson<B> flatMap(Function<A, DecodeJson<B>> f) {
        return value -> {
            DecodeResult<A> result = this.fromJson(value);
            return result.flatMap(a -> f.apply(a).fromJson(value));
        };
    }

    default DecodeJson<A> withDefaultValue(A value) {
        return new DecodeJsonWithDefault<>(this, value);
    }

    class DecodeJsonWithDefault<A> implements DecodeJson<A> {
        private final DecodeJson<A> delegate;
        private final A value;

        public DecodeJsonWithDefault(DecodeJson<A> delegate, A value) {
            this.delegate = delegate;
            this.value = value;
        }

        @Override
        public DecodeResult<A> fromJson(Json.JValue value) {
            return delegate.fromJson(value);
        }

        @Override
        public Option<A> defaultValue() {
            return Option.some(value);
        }
    }
}
