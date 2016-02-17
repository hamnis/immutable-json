package net.hamnaberg.json.extract;

import net.hamnaberg.json.Codecs;
import net.hamnaberg.json.DecodeJson;
import net.hamnaberg.json.EncodeJson;
import net.hamnaberg.json.Json;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class TypedField<A> {
    public final String name;
    public final DecodeJson<A> decoder;

    public TypedField(String name, DecodeJson<A> decoder) {
        this.name = name;
        this.decoder = decoder;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), name);
    }

    public <B> TypedField<B> map(Function<A, B> f) {
        return new TypedField<B>(name, decoder.map(f)) {};
    }

    public static TStringField TString(String name) {
        return new TStringField(name);
    }

    public static TIntegerField TInt(String name) {
        return new TIntegerField(name);
    }

    public static TDoubleField TDouble(String name) {
        return new TDoubleField(name);
    }

    public static TLongField TLong(String name) {
        return new TLongField(name);
    }

    public static TBooleanField TBoolean(String name) {
        return new TBooleanField(name);
    }

    public static TJArrayField TJArray(String name) {
        return new TJArrayField(name);
    }

    public static TJObjectField TJObject(String name) {
        return new TJObjectField(name);
    }

    public static class TStringField extends TypedField<String> {
        public TStringField(String name) {
            super(name, Codecs.StringCodec);
        }
    }

    public static class TIntegerField extends TypedField<Integer> {
        public TIntegerField(String name) {
            super(name, Codecs.intCodec);
        }
    }

    public static class TDoubleField extends TypedField<Double> {
        public TDoubleField(String name) {
            super(name, Codecs.doubleCodec);
        }
    }

    public static class TLongField extends TypedField<Long> {
        public TLongField(String name) {
            super(name, Codecs.longCodec);
        }
    }

    private static class TBooleanField extends TypedField<Boolean> {
        public TBooleanField(String name) {
            super(name, Codecs.booleanCodec);
        }
    }

    public static class TJArrayField extends TypedField<Json.JArray> {
        public TJArrayField(String name) {
            super(name, Json.JValue::asJsonArray);
        }

        public <B> TypedField<List<B>> mapToList(Function<Json.JValue, B> f) {
            return new TypedField<List<B>>(name, decoder.map(ja -> ja.mapToList(f))) {};
        }
        public <B> TypedField<List<B>> mapToOptionalList(Function<Json.JValue, Optional<B>> f) {
            Function<Optional<B>, List<B>> toList = opt -> opt.isPresent() ? Collections.singletonList(opt.get()) : Collections.emptyList();
            return new TypedField<List<B>>(name, decoder.map(ja -> ja.flatMapToList(f.andThen(toList)))) {};
        }
    }

    public static class TJObjectField extends TypedField<Json.JObject> {
        public TJObjectField(String name) {
            super(name, Json.JValue::asJsonObject);
        }
    }
}
