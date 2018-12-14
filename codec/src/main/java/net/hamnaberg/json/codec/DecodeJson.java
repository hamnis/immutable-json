package net.hamnaberg.json.codec;

import net.hamnaberg.json.Json;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface DecodeJson<A> {
    DecodeResult<A> fromJson(Json.JValue value);

    default Optional<A> defaultValue() {
        return Optional.empty();
    }

    default A fromJsonUnsafe(Json.JValue value) {
        return fromJson(value).unsafeGet();
    }

    default <B> DecodeJson<B> map(Function<A, B> f) {
        return (json) -> this.fromJson(json).map(f);
    }

    default <B> DecodeJson<B> tryMap(Function<A, Callable<B>> f) {
        return json -> this.fromJson(json).map(f).flatMap(DecodeResult::fromCallable);
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

    default DecodeJson<A> or(DecodeJson<A> orElse) {
        return value -> fromJson(value).fold(aFail -> orElse.fromJson(value).fold(bFail -> DecodeResult.fail(aFail + " " + bFail), DecodeResult::ok), DecodeResult::ok);
    }

    default <B> DecodeJson<Map.Entry<A, B>> and(DecodeJson<B> next) {
        return value -> {
            DecodeResult<A> aRes = fromJson(value);
            DecodeResult<B> bRes = next.fromJson(value);
            return aRes.flatMap(a -> bRes.map(b -> Map.entry(a, b)));
        };
    }

    default DecodeJson<Optional<A>> option() {
        return Decoders.optionalDecoder(this);
    }

    default DecodeJson<List<A>> list() {
        return Decoders.listDecoder(this);
    }

    default FieldDecoder<A> fieldDecoder(String name) {
        return FieldDecoder.typedFieldOf(name, this);
    }

    default DecodeJson<A> filter(Predicate<A> p) {
        return filter(p, () -> "Filter failed");
    }

    default DecodeJson<A> filter(Predicate<A> p, Supplier<String> errorSupplier) {
        return value -> fromJson(value).filter(p, errorSupplier);
    }

    static <A> DecodeJson<List<A>> sequence(List<DecodeJson<A>> toSequence) {
        return value -> DecodeResult.sequence(
                toSequence.stream().map(d -> d.fromJson(value)).collect(Collectors.toUnmodifiableList())
        );
    }

    static <T, A, R> DecodeJson<R> sequence(final Iterable<DecodeJson<T>> results, final Collector<T, A, R> collector) {
        return value ->
                DecodeResult.sequence(
                        () -> StreamSupport.stream(results.spliterator(), false).map(d -> d.fromJson(value)).iterator(),
                        collector
                );
    }

    static <A> DecodeJson<A> successful(A value) {
        return result(DecodeResult.ok(value));
    }

    static <A> DecodeJson<A> failure(String message) {
        return result(DecodeResult.fail(message));
    }

    static <A> DecodeJson<A> result(DecodeResult<A> value) {
        return ignore -> value;
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
            return res.fold(ignore -> DecodeResult.ok(defaultValue), DecodeResult::ok);
        }

        @Override
        public Optional<A> defaultValue() {
            return Optional.of(defaultValue);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + String.format(" {delegate=%s, default=%s}", delegate, defaultValue);
        }
    }
}
