package net.hamnaberg.json.codec;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import net.hamnaberg.json.Json;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface DecodeJson<A> {
    DecodeResult<A> fromJson(Json.JValue value);

    default Option<A> defaultValue() {
        return Option.none();
    }

    default A fromJsonUnsafe(Json.JValue value) {
        return fromJson(value).unsafeGet();
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

    default DecodeJson<A> or(DecodeJson<A> orElse) {
        return value -> fromJson(value).fold(a -> a, aFail -> orElse.fromJson(value).fold(a -> a, bFail -> DecodeResult.fail(aFail.message + " " + bFail.message)));
    }

    default <L> DecodeJson<Either<L, A>> either(DecodeJson<L> left) {
        return either(left, this);
    }

    default <B> DecodeJson<Tuple2<A, B>> and(DecodeJson<B> next) {
        return value -> {
            DecodeResult<A> aRes = fromJson(value);
            DecodeResult<B> bRes = next.fromJson(value);
            return aRes.flatMap(a -> bRes.map(b -> Tuple.of(a, b)));
        };
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
        return value -> DecodeResult.sequence(toSequence.map(d -> d.fromJson(value)));
    }

    static <L, R> DecodeJson<Either<L, R>> either(DecodeJson<L> left, DecodeJson<R> right) {
        return right.<Either<L, R>>map(Either::right).or(left.map(Either::left));
    }

    static <A> DecodeJson<A> successful(A value) {
        return ignore -> DecodeResult.ok(value);
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
