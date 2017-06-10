package net.hamnaberg.json.codec;

import io.vavr.Value;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import net.hamnaberg.json.Json;

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
        return typedFieldOf(name, json -> decoder.fromJson(json).flatMap(a -> DecodeResult.fromOption(f.apply(a).toOption())), Option.none());
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

    public static TJArrayField TJArray(String name) {
        return new TJArrayField(name);
    }

    public static TJObjectField TJObject(String name) {
        return new TJObjectField(name);
    }

    public static <B> FieldDecoder<Option<B>> TOptional(String name, DecodeJson<B> decoder) {
        return typedFieldOf(name, json -> DecodeResult.ok(decoder.fromJson(json).toOption()), Option.some(Option.none()));
    }

    public static <B> FieldDecoder<B> typedFieldOf(String name, DecodeJson<B> decoder) {
        return new FieldDecoder<B>(name, decoder) {};
    }

    public static <B> FieldDecoder<B> typedFieldOf(String name, DecodeJson<B> decoder, Option<B> defaultValue) {
        return new FieldDecoder<B>(name, defaultValue.map(decoder::withDefaultValue).getOrElse(decoder)) {};
    }

    public static class TJArrayField extends FieldDecoder<Json.JArray> {
        public TJArrayField(String name) {
            super(name, v -> DecodeResult.fromOption(v.asJsonArray()));
        }

        public <B> FieldDecoder<List<B>> mapToList(Function<Json.JValue, B> f) {
            return typedFieldOf(name, decoder.map(ja -> ja.mapToList(f)), Option.none());
        }
        public <B> FieldDecoder<List<B>> mapToOptionalList(Function<Json.JValue, Option<B>> f) {
            return typedFieldOf(name, decoder.map(ja -> ja.flatMapToList(f.andThen(Value::toList))), Option.none());
        }
    }

    public static class TJObjectField extends FieldDecoder<Json.JObject> {
        public TJObjectField(String name) {
            super(name, v -> DecodeResult.fromOption(v.asJsonObject()));
        }

        public <B> FieldDecoder<B> decodeTo(DecodeJson<B> mapper) {
            return typedFieldOf(name, json -> mapper.fromJson(json.asJsonObjectOrEmpty()), Option.none());
        }
    }
}
