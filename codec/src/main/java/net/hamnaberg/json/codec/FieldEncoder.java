package net.hamnaberg.json.codec;

import io.vavr.collection.List;
import io.vavr.control.Option;
import net.hamnaberg.json.Json;

import java.util.Optional;
import java.util.function.Function;

public final class FieldEncoder<A> {
    public final String name;
    public final EncodeJson<A> encoder;

    private FieldEncoder(String name, EncodeJson<A> encoder) {
        this.name = name;
        this.encoder = encoder;
    }

    @Override
    public String toString() {
        return String.format("FieldDecoder(%s)", name);
    }

    public <B> FieldEncoder<B> contramap(Function<B, A> f) {
        return typedFieldOf(name, encoder.contramap(f));
    }

    public Json.JValue toJson(A value) {
        return encoder.toJson(value);
    }

    public static FieldEncoder<String> EString(String name) {
        return typedFieldOf(name, Encoders.EString);
    }

    public static FieldEncoder<Integer> EInt(String name) {
        return typedFieldOf(name, Encoders.EInt);
    }

    public static FieldEncoder<Double> EDouble(String name) {
        return typedFieldOf(name, Encoders.EDouble);
    }

    public static FieldEncoder<Long> ELong(String name) {
        return typedFieldOf(name, Encoders.ELong);
    }

    public static FieldEncoder<Boolean> EBoolean(String name) {
        return typedFieldOf(name, Encoders.EBoolean);
    }

    public static <A> FieldEncoder<List<A>> EList(String name, EncodeJson<A> encoder) {
        return typedFieldOf(name, Encoders.listEncoder(encoder));
    }

    public static <A> FieldEncoder<java.util.List<A>> EJavaList(String name, EncodeJson<A> encoder) {
        return typedFieldOf(name, Encoders.javaListEncoder(encoder));
    }

    public static <B> FieldEncoder<Option<B>> EOptional(String name, EncodeJson<B> encoder) {
        return typedFieldOf(name, Encoders.OptionEncoder(encoder));
    }

    public static <B> FieldEncoder<Optional<B>> EJavaOptional(String name, EncodeJson<B> encoder) {
        return typedFieldOf(name, Encoders.OptionalEncoder(encoder));
    }

    public static <B> FieldEncoder<B> typedFieldOf(String name, EncodeJson<B> encoder) {
        return new FieldEncoder<>(name, encoder);
    }
}
