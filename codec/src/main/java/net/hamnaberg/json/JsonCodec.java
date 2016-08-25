package net.hamnaberg.json;

import javaslang.control.Try;

import java.util.function.Function;

public interface JsonCodec<A> extends EncodeJson<A>, DecodeJson<A> {
    default <B> JsonCodec<B> xmap(Function<A, B> f, Function<B, A> g) {
        return JsonCodec.lift(value -> fromJson(value).map(f), value -> toJson(g.apply(value)));
    }

    default <B> JsonCodec<B> narrow(Function<A, Try<B>> f, Function<B, A> g) {
        return JsonCodec.lift(tryMap(f), value -> toJson(g.apply(value)));
    }

    default <B> JsonCodec<B> narrowBoth(Function<A, Try<B>> f, Function<B, Try<A>> g) {
        return JsonCodec.lift(tryMap(f), value -> g.apply(value).toOption().flatMap(this::toJson));
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
