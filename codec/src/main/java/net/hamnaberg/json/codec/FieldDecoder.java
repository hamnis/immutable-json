package net.hamnaberg.json.codec;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.Optional;
import java.util.function.Function;

public abstract class FieldDecoder<A> {
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
        return typedFieldOf(name, decoder.map(f), Option.none());
    }

    public <B> FieldDecoder<B> flatMap(Function<A, FieldDecoder<B>> f) {
        DecodeJson<B> bdecoder = json -> decoder.flatMap(a -> f.apply(a).decoder).fromJson(json);
        return typedFieldOf(name, bdecoder, Option.none());
    }

    public <B> FieldDecoder<B> narrow(Function<A, Try<B>> f) {
        return typedFieldOf(
                name,
                json -> decoder.tryMap(f).fromJson(json).fold(
                        err -> DecodeResult.fail(String.format("Decode for '%s' failed with %s", name, err)),
                        DecodeResult::ok
                ),
                Option.none()
        );
    }

    public <B> FieldDecoder<B> tryNarrow(Function<A, B> f) {
        return narrow(a -> Try.of(() -> f.apply(a)));
    }

    public FieldDecoder<A> withDefaultValue(A defaultValue) {
        return typedFieldOf(this.name, this.decoder, Option.some(defaultValue));
    }

    public static FieldDecoder<String> TString(String name) {
        return typedFieldOf(name, Decoders.DString, Option.none());
    }

    public static FieldDecoder<Integer> TInt(String name) {
        return typedFieldOf(name, Decoders.DInt, Option.none());
    }

    public static FieldDecoder<Double> TDouble(String name) {
        return typedFieldOf(name, Decoders.DDouble, Option.none());
    }

    public static FieldDecoder<Long> TLong(String name) {
        return typedFieldOf(name, Decoders.DLong, Option.none());
    }

    public static FieldDecoder<Boolean> TBoolean(String name) {
        return typedFieldOf(name, Decoders.DBoolean, Option.none());
    }

    public static <B> FieldDecoder<Option<B>> TOptional(String name, DecodeJson<B> decoder) {
        return typedFieldOf(name, Decoders.OptionDecoder(decoder), Option.some(Option.none()));
    }

    public static <B> FieldDecoder<Optional<B>> TJavaOptional(String name, DecodeJson<B> decoder) {
        return TOptional(name, decoder).map(Option::toJavaOptional);
    }

    public static <B> FieldDecoder<List<B>> TList(String name, DecodeJson<B> decoder) {
        return typedFieldOf(name, Decoders.listDecoder(decoder));
    }

    public static <B> FieldDecoder<java.util.List<B>> TJavaList(String name, DecodeJson<B> decoder) {
        return typedFieldOf(name, Decoders.javaListDecoder(decoder));
    }

    public static <B> FieldDecoder<B> typedFieldOf(String name, DecodeJson<B> decoder) {
        return new FieldDecoder<B>(name, decoder) {};
    }

    public static <B> FieldDecoder<B> typedFieldOf(String name, DecodeJson<B> decoder, Option<B> defaultValue) {
        return new FieldDecoder<B>(name, defaultValue.map(decoder::withDefaultValue).getOrElse(decoder)) {};
    }
}
