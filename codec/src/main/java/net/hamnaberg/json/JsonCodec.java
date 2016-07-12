package net.hamnaberg.json;

import javaslang.control.Option;
import javaslang.control.Try;

import java.util.function.Function;

public interface JsonCodec<A> extends EncodeJson<A>, DecodeJson<A> {
    default <B> JsonCodec<B> xmap(Function<A, B> f, Function<B, A> g) {
        JsonCodec<A> that = this;
        return new JsonCodec<B>() {
            @Override
            public DecodeResult<B> fromJson(Json.JValue value) {
                return that.fromJson(value).map(f);
            }

            @Override
            public Option<Json.JValue> toJson(B value) {
                return that.toJson(g.apply(value));
            }
        };
    }

    default <B> JsonCodec<B> narrow(Function<A, Try<B>> f, Function<B, A> g) {
        JsonCodec<A> that = this;
        return new JsonCodec<B>() {
            @Override
            public DecodeResult<B> fromJson(Json.JValue value) {
                return that.fromJson(value).map(f).flatMap(meh -> meh.map(DecodeResult::ok).getOrElseGet(t -> DecodeResult.fail(t.getMessage())));
            }

            @Override
            public Option<Json.JValue> toJson(B value) {
                return that.toJson(g.apply(value));
            }
        };
    }

    default <B> JsonCodec<B> narrowBoth(Function<A, Try<B>> f, Function<B, Try<A>> g) {
        JsonCodec<A> that = this;
        return new JsonCodec<B>() {
            @Override
            public DecodeResult<B> fromJson(Json.JValue value) {
                return that.fromJson(value).map(f).flatMap(meh -> meh.map(DecodeResult::ok).getOrElseGet(t -> DecodeResult.fail(t.getMessage())));
            }

            @Override
            public Option<Json.JValue> toJson(B value) {
                Try<A> apply = g.apply(value);
                return apply.toOption().flatMap(that::toJson);
            }
        };
    }

    default <B> JsonCodec<B> tryNarrow(Function<A, B> f, Function<B, A> g) {
        return narrow(a -> Try.of(() -> f.apply(a)), g);
    }

    default <B> JsonCodec<B> tryNarrowBoth(Function<A, B> f, Function<B, A> g) {
        return narrowBoth(a -> Try.of(() -> f.apply(a)), b -> Try.of(() -> g.apply(b)));
    }

    static <A> JsonCodec<A> lift(DecodeJson<A> decoder, EncodeJson<A> encoder) {
        return new DefaultJsonCodec<>(decoder, encoder);
    }
}

class DefaultJsonCodec<A> implements JsonCodec<A> {
    private DecodeJson<A> decoder;
    private EncodeJson<A> encoder;

    DefaultJsonCodec(DecodeJson<A> decoder, EncodeJson<A> encoder) {
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    public DecodeResult<A> fromJson(Json.JValue value) {
        return decoder.fromJson(value);
    }

    @Override
    public Option<Json.JValue> toJson(A value) {
        return encoder.toJson(value);
    }

    @Override
    public Option<A> defaultValue() {
        return decoder.defaultValue();
    }

    public String toString() {
        return String.format("DefaultCodec(decoder=%s, encoder=%s)", decoder, encoder);
    }
}
