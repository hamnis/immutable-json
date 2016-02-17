package net.hamnaberg.json.extract;

import net.hamnaberg.json.Codecs;
import net.hamnaberg.json.DecodeJson;
import net.hamnaberg.json.EncodeJson;
import net.hamnaberg.json.Json;

import java.util.Optional;

public abstract class TypedField<A> {
    public final Class<A> type;
    public final String name;
    public final DecodeJson<A> decoder;

    public TypedField(Class<A> type, String name, DecodeJson<A> decoder) {
        this.type = type;
        this.name = name;
        this.decoder = decoder;
    }

    @Override
    public String toString() {
        return String.format("TypedField(%s, %s)", name, type.getSimpleName());
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

    public static TJArrayField TJArray(String name) {
        return new TJArrayField(name);
    }

    public static TJObjectField TJObject(String name) {
        return new TJObjectField(name);
    }

    public static class TStringField extends TypedField<String> {
        public TStringField(String name) {
            super(String.class, name, Codecs.StringCodec);
        }
    }

    public static class TIntegerField extends TypedField<Integer> {
        public TIntegerField(String name) {
            super(Integer.class, name, Codecs.intCodec);
        }
    }

    public static class TDoubleField extends TypedField<Double> {
        public TDoubleField(String name) {
            super(Double.class, name, Codecs.doubleCodec);
        }
    }

    public static class TLongField extends TypedField<Long> {
        public TLongField(String name) {
            super(Long.class, name, Codecs.longCodec);
        }
    }

    public static class TJArrayField extends TypedField<Json.JArray> {
        public TJArrayField(String name) {
            super(Json.JArray.class, name, Json.JValue::asJsonArray);
        }
    }

    public static class TJObjectField extends TypedField<Json.JObject> {
        public TJObjectField(String name) {
            super(Json.JObject.class, name, Json.JValue::asJsonObject);
        }
    }
}
