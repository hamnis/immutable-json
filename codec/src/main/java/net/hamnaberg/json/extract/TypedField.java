package net.hamnaberg.json.extract;

import io.vavr.Value;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import net.hamnaberg.json.codec.Decoders;
import net.hamnaberg.json.codec.DecodeJson;
import net.hamnaberg.json.codec.DecodeResult;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.codec.FieldDecoder;

import java.util.function.Function;

@Deprecated
public abstract class TypedField<A> {
    public final String name;
    public final DecodeJson<A> decoder;

    private TypedField(String name, DecodeJson<A> decoder) {
        this.name = name;
        this.decoder = decoder;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), name);
    }

    public <B> TypedField<B> map(Function<A, B> f) {
        return typedFieldOf(name, decoder.map(f), Option.none());
    }

    public <B> TypedField<B> flatMap(Function<A, TypedField<B>> f) {
        DecodeJson<B> bdecoder = json -> decoder.flatMap(a -> f.apply(a).decoder).fromJson(json);
        return typedFieldOf(name, bdecoder, Option.none());
    }

    public <B> TypedField<B> narrow(Function<A, Try<B>> f) {
        return typedFieldOf(name, json -> decoder.fromJson(json).flatMap(a -> DecodeResult.fromOption(f.apply(a).toOption())), Option.none());
    }

    public <B> TypedField<B> tryNarrow(Function<A, B> f) {
        return narrow(a -> Try.of(() -> f.apply(a)));
    }

    public FieldDecoder<A> toFieldDecoder() {
        return FieldDecoder.typedFieldOf(name, decoder);
    }

    public TypedField<A> withDefaultValue(A defaultValue) {
        return typedFieldOf(this.name, this.decoder, Option.some(defaultValue));
    }

    public static TypedField<String> TString(String name) {
        return typedFieldOf(name, Decoders.DString, Option.none());
    }

    public static TypedField<Integer> TInt(String name) {
        return typedFieldOf(name, Decoders.DInt, Option.none());
    }

    public static TypedField<Double> TDouble(String name) {
        return typedFieldOf(name, Decoders.DDouble, Option.none());
    }

    public static TypedField<Long> TLong(String name) {
        return typedFieldOf(name, Decoders.DLong, Option.none());
    }

    public static TypedField<Boolean> TBoolean(String name) {
        return typedFieldOf(name, Decoders.DBoolean, Option.none());
    }

    public static TJArrayField TJArray(String name) {
        return new TJArrayField(name);
    }

    public static TJObjectField TJObject(String name) {
        return new TJObjectField(name);
    }

    public static <B> TypedField<Option<B>> TOptional(String name, Extractor<B> extractor) {
        return TOptional(name, extractor.decoder());
    }

    public static <B> TypedField<Option<B>> TOptional(String name, DecodeJson<B> decoder) {
        return typedFieldOf(name, json -> DecodeResult.ok(decoder.fromJson(json).toOption()), Option.some(Option.none()));
    }

    public static <B> TypedField<B> typedFieldOf(String name, DecodeJson<B> decoder) {
        return new TypedField<B>(name, decoder) {};
    }

    public static <B> TypedField<B> typedFieldOf(String name, DecodeJson<B> decoder, Option<B> defaultValue) {
        return new TypedField<B>(name, defaultValue.map(decoder::withDefaultValue).getOrElse(decoder)) {};
    }

    public static <B> TypedField<B> typedFieldOfExtract(String name, Extractor<B> decoder, Option<B> defaultValue) {
        return typedFieldOf(name, decoder.decoder(), defaultValue);
    }

    public static class TJArrayField extends TypedField<Json.JArray> {
        public TJArrayField(String name) {
            super(name, v -> DecodeResult.fromOption(v.asJsonArray()));
        }

        public <B> TypedField<List<B>> mapToList(Function<Json.JValue, B> f) {
            return typedFieldOf(name, decoder.map(ja -> ja.mapToList(f)), Option.none());
        }
        public <B> TypedField<List<B>> mapToOptionalList(Function<Json.JValue, Option<B>> f) {
            return typedFieldOf(name, decoder.map(ja -> ja.flatMapToList(f.andThen(Value::toList))), Option.none());
        }
    }

    public static class TJObjectField extends TypedField<Json.JObject> {
        public TJObjectField(String name) {
            super(name, v -> DecodeResult.fromOption(v.asJsonObject()));
        }

        public <B> TypedField<B> extractTo(Extractor<B> mapper) {
            return typedFieldOf(name, json -> mapper.apply(json.asJsonObjectOrEmpty()), Option.none());
        }
    }
}
