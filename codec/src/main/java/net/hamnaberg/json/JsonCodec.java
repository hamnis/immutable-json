package net.hamnaberg.json;

import javaslang.control.Option;
import javaslang.control.Try;

import java.util.function.Function;

public interface JsonCodec<A> extends EncodeJson<A>, DecodeJson<A> {
    default <B> JsonCodec<B> xmap(Function<A, B> f, Function<B, A> g) {
        JsonCodec<A> that = this;
        return new JsonCodec<B>() {
            @Override
            public Option<B> fromJson(Json.JValue value) {
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
            public Option<B> fromJson(Json.JValue value) {
                return that.fromJson(value).map(f).flatMap(Try::toOption);
            }

            @Override
            public Option<Json.JValue> toJson(B value) {
                return that.toJson(g.apply(value));
            }
        };
    }

    default <B> JsonCodec<B> tryNarrow(Function<A, B> f, Function<B, A> g) {
        return narrow(a -> Try.of(() -> f.apply(a)), g);
    }
}
