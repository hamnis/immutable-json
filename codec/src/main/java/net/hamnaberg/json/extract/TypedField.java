package net.hamnaberg.json.extract;

import javaslang.Value;
import javaslang.collection.List;
import javaslang.control.Option;
import net.hamnaberg.json.Codecs;
import net.hamnaberg.json.DecodeJson;
import net.hamnaberg.json.DecodeResult;
import net.hamnaberg.json.Json;

import java.util.function.Function;

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
        return typedFieldOf(name, decoder.map(f));
    }

    public static TypedField<String> TString(String name) {
        return typedFieldOf(name, Codecs.StringCodec);
    }

    public static TypedField<Integer> TInt(String name) {
        return typedFieldOf(name, Codecs.intCodec);
    }

    public static TypedField<Double> TDouble(String name) {
        return typedFieldOf(name, Codecs.doubleCodec);
    }

    public static TypedField<Long> TLong(String name) {
        return typedFieldOf(name, Codecs.longCodec);
    }

    public static TypedField<Boolean> TBoolean(String name) {
        return typedFieldOf(name, Codecs.booleanCodec);
    }

    public static TJArrayField TJArray(String name) {
        return new TJArrayField(name);
    }

    public static TJObjectField TJObject(String name) {
        return new TJObjectField(name);
    }

    public static <B> TypedField<B> typedFieldOf(String name, DecodeJson<B> decoder) {
        return new TypedField<B>(name, decoder) {};
    }

    public static class TJArrayField extends TypedField<Json.JArray> {
        public TJArrayField(String name) {
            super(name, v -> DecodeResult.fromOption(v.asJsonArray()));
        }

        public <B> TypedField<List<B>> mapToList(Function<Json.JValue, B> f) {
            return typedFieldOf(name, decoder.map(ja -> ja.mapToList(f)));
        }
        public <B> TypedField<List<B>> mapToOptionalList(Function<Json.JValue, Option<B>> f) {
            return typedFieldOf(name, decoder.map(ja -> ja.flatMapToList(f.andThen(Value::toList))));
        }
    }

    public static class TJObjectField extends TypedField<Json.JObject> {
        public TJObjectField(String name) {
            super(name, v -> DecodeResult.fromOption(v.asJsonObject()));
        }

        public <B> TypedField<B> extractTo(Extractor<B> mapper) {
            return typedFieldOf(name, json -> mapper.apply(json.asJsonObjectOrEmpty()));
        }
    }
}
