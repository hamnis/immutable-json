package net.hamnaberg.json.codec;

import io.vavr.control.Option;
import io.vavr.control.Try;
import net.hamnaberg.json.Json;

import java.util.function.Function;

public interface JsonCodec<A> extends EncodeJson<A>, DecodeJson<A> {
    default <B> JsonCodec<B> xmap(Function<A, B> f, Function<B, A> g) {
        return JsonCodec.lift(value -> fromJson(value).map(f), value -> toJson(g.apply(value)));
    }

    default <B> JsonCodec<B> xmapi(Iso<A, B> iso) {
        return xmap(iso::get, iso::reverseGet);
    }

    default <B> JsonCodec<B> narrow(Function<A, Try<B>> f, Function<B, A> g) {
        return JsonCodec.lift(tryMap(f), value -> toJson(g.apply(value)));
    }

    default <B> JsonCodec<B> tryNarrow(Function<A, B> f, Function<B, A> g) {
        return narrow(a -> Try.of(() -> f.apply(a)), g);
    }

    /**
     * @deprecated use {@link #field(String)} instead
     */
    @Deprecated
    default NamedJsonCodec<A> named(String name) {
        return NamedJsonCodec.of(name, this);
    }

    default NamedJsonCodec<A> field(String name) {
        return NamedJsonCodec.of(name, this);
    }

    @Override
    default JsonCodec<A> withDefaultValue(A defaultValue) {
        return lift(new DecodeJsonWithDefault<>(this, defaultValue), this);
    }

    static <A> JsonCodec<A> lift(DecodeJson<A> decoder, EncodeJson<A> encoder) {
        return lift(decoder, encoder, Option.none());
    }

    static <A> JsonCodec<A> lift(DecodeJson<A> decoder, EncodeJson<A> encoder, Option<A> defaultValue) {
        return new DefaultJsonCodec<>(defaultValue.map(decoder::withDefaultValue).getOrElse(decoder), encoder);
    }
}
