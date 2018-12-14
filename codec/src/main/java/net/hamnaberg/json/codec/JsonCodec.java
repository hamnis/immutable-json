package net.hamnaberg.json.codec;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;

public interface JsonCodec<A> extends EncodeJson<A>, DecodeJson<A> {
    default <B> JsonCodec<B> xmap(Function<A, B> f, Function<B, A> g) {
        return JsonCodec.lift(value -> fromJson(value).map(f), value -> toJson(g.apply(value)));
    }

    default <B> JsonCodec<B> xmapi(Iso<A, B> iso) {
        return xmap(iso::get, iso::reverseGet);
    }

    default <B> JsonCodec<B> narrow(Function<A, Callable<B>> f, Function<B, A> g) {
        return JsonCodec.lift(tryMap(f), value -> toJson(g.apply(value)));
    }

    default <B> JsonCodec<B> tryNarrow(Function<A, B> f, Function<B, A> g) {
        return narrow(a -> () -> f.apply(a), g);
    }

    @Override
    default JsonCodec<Optional<A>> option() {
        return Codecs.optionalCodec(this);
    }

    @Override
    default JsonCodec<List<A>> list() {
        return Codecs.listCodec(this);
    }

    default NamedJsonCodec<A> field(String name) {
        return NamedJsonCodec.of(name, this);
    }

    @Override
    default JsonCodec<A> withDefaultValue(A defaultValue) {
        return lift(new DecodeJsonWithDefault<>(this, defaultValue), this);
    }

    static <A> JsonCodec<A> lift(DecodeJson<A> decoder, EncodeJson<A> encoder) {
        return lift(decoder, encoder, Optional.empty());
    }

    static <A> JsonCodec<A> lift(DecodeJson<A> decoder, EncodeJson<A> encoder, Optional<A> defaultValue) {
        return new DefaultJsonCodec<>(defaultValue.map(decoder::withDefaultValue).orElse(decoder), encoder);
    }
}
