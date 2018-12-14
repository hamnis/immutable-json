package net.hamnaberg.json.codec;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;

public final class FieldDecoder<A> {
    public final String name;
    public final DecodeJson<A> decoder;

    private FieldDecoder(String name, DecodeJson<A> decoder) {
        this.name = name;
        this.decoder = decoder;
    }

    @Override
    public String toString() {
        return String.format("FieldDecoder(%s)", name);
    }

    public <B> FieldDecoder<B> map(Function<A, B> f) {
        return typedFieldOf(name, decoder.map(f), Optional.empty());
    }

    public <B> FieldDecoder<B> flatMap(Function<A, FieldDecoder<B>> f) {
        DecodeJson<B> bdecoder = json -> decoder.flatMap(a -> f.apply(a).decoder).fromJson(json);
        return typedFieldOf(name, bdecoder, Optional.empty());
    }

    public <B> FieldDecoder<B> narrow(Function<A, Callable<B>> f) {
        return typedFieldOf(
                name,
                json -> decoder.tryMap(f).fromJson(json).fold(
                        err -> DecodeResult.fail(String.format("Decode for '%s' failed with %s", name, err)),
                        DecodeResult::ok
                ),
                Optional.empty()
        );
    }

    public <B> FieldDecoder<B> tryNarrow(Function<A, B> f) {
        return narrow(a -> () -> f.apply(a));
    }

    public FieldDecoder<A> withDefaultValue(A defaultValue) {
        return typedFieldOf(this.name, this.decoder, Optional.of(defaultValue));
    }

    public static FieldDecoder<String> TString(String name) {
        return typedFieldOf(name, Decoders.DString, Optional.empty());
    }

    public static FieldDecoder<Integer> TInt(String name) {
        return typedFieldOf(name, Decoders.DInt, Optional.empty());
    }

    public static FieldDecoder<Double> TDouble(String name) {
        return typedFieldOf(name, Decoders.DDouble, Optional.empty());
    }

    public static FieldDecoder<Long> TLong(String name) {
        return typedFieldOf(name, Decoders.DLong, Optional.empty());
    }

    public static FieldDecoder<Boolean> TBoolean(String name) {
        return typedFieldOf(name, Decoders.DBoolean, Optional.empty());
    }

    public static <B> FieldDecoder<Optional<B>> TOptional(String name, DecodeJson<B> decoder) {
        return typedFieldOf(name, Decoders.optionalDecoder(decoder), Optional.of(Optional.empty()));
    }

    public static <B> FieldDecoder<List<B>> TList(String name, DecodeJson<B> decoder) {
        return typedFieldOf(name, Decoders.listDecoder(decoder));
    }

    public static <B> FieldDecoder<B> typedFieldOf(String name, DecodeJson<B> decoder) {
        return new FieldDecoder<B>(name, decoder);
    }

    public static <B> FieldDecoder<B> typedFieldOf(String name, DecodeJson<B> decoder, Optional<B> defaultValue) {
        return new FieldDecoder<B>(name, defaultValue.map(decoder::withDefaultValue).orElse(decoder));
    }
}
