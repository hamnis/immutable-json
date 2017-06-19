package net.hamnaberg.json.codec.reflection;

import io.vavr.collection.List;
import io.vavr.control.Option;
import net.hamnaberg.json.codec.Codecs;
import net.hamnaberg.json.codec.DecodeResult;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.codec.JsonCodec;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;

public final class ReflectionCodec<A> implements JsonCodec<A> {
    private final Class<A> type;
    private final Map<String, JsonCodec<?>> codecs;
    private final List<Param> fields;
    private final Factory<A> factory;

    private static Map<Class<?>, JsonCodec<?>> defaultCodecs;

    static {
        Map<Class<?>, JsonCodec<?>> codecs = new HashMap<>();
        codecs.put(String.class, Codecs.CString);
        codecs.put(Integer.class, Codecs.CInt);
        codecs.put(int.class, Codecs.CInt);
        codecs.put(double.class, Codecs.CDouble);
        codecs.put(long.class, Codecs.CLong);
        codecs.put(Long.class, Codecs.CLong);
        codecs.put(Double.class, Codecs.CDouble);
        codecs.put(BigInteger.class, Codecs.CNumber);
        codecs.put(Number.class, Codecs.CNumber);
        codecs.put(Boolean.class, Codecs.CBoolean);
        defaultCodecs = Collections.unmodifiableMap(codecs);
    }

    public ReflectionCodec(Class<A> type) {
        this(type, Collections.emptyMap());
    }

    public ReflectionCodec(Class<A> type, Map<String, JsonCodec<?>> codecs) {
        this(type, codecs, (p) -> true, Option.none());
    }

    public ReflectionCodec(Class<A> type, Map<String, JsonCodec<?>> codecs, Predicate<Param> predicate) {
        this(type, codecs, predicate, Option.none());
    }

    public ReflectionCodec(Class<A> type, Map<String, JsonCodec<?>> codecs, Predicate<Param> predicate, Option<String> factoryMethod) {
        this.type = type;
        this.codecs = codecs;
        this.fields = getFields(type).filter(predicate);
        this.factory = factoryMethod.map(n -> Factory.factory(type, n, fields)).getOrElse(Factory.constructor(type, fields));
    }

    @Override
    public Json.JValue toJson(A value) {
        Map<String, Json.JValue> map = new LinkedHashMap<>();
        for (Param field : fields) {
            Option<JsonCodec<Object>> codec = getCodec(field);
            Option<Object> optValue = field.get(value);
            Option<Json.JValue> opt = optValue.flatMap(o -> codec.map(c -> c.toJson(o)));
            opt.forEach(v -> map.put(field.getName(), v));
        }
        return map.isEmpty() ? Json.jNull() : Json.jObject(map);
    }

    @Override
    public DecodeResult<A> fromJson(Json.JValue value) {
        Json.JObject object = value.asJsonObjectOrEmpty();
        ArrayList<Object> arguments = new ArrayList<>();

        for (Param field : fields) {
            JsonCodec<Object> codec = getCodec(field).getOrElseThrow(() -> {
                throw new NoSuchElementException("Missing codec for " + field.getName());
            });
            DecodeResult<Object> result = DecodeResult.decode(object, field.getName(), codec);
            result.forEach(arguments::add);
        }

        try {
            return DecodeResult.ok(this.factory.invoke(List.ofAll(arguments)));
        } catch (Exception e) {
            return DecodeResult.fail(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Option<JsonCodec<Object>> getCodec(Param field) {
        return Option.of(
                (JsonCodec<Object>) codecs.getOrDefault(field.getName(), defaultCodecs.get(field.getType()))
        );
    }


    @Override
    public String toString() {
        return String.format("ReflectionCodec(%s)", type.getName());
    }

    private static String getterOf(String field) {
        return "get" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }

    private static <A> List<Param> getFields(Class<A> type) {
        Field[] declared = type.getDeclaredFields();
        ArrayList<Param> fields = new ArrayList<>(declared.length);
        for (Field field : declared) {
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers) && Modifier.isPublic(modifiers)) {
                fields.add(new FieldParam(field.getName(), field));
            }
        }
        if (fields.isEmpty()) {
            for (Field field : declared) {
                try {
                    Method method = type.getDeclaredMethod(getterOf(field.getName()));
                    if (method != null) {
                        fields.add(new MethodParam(field.getName(), method));
                    }
                } catch (NoSuchMethodException ignore) {
                }
            }
        }
        return List.ofAll(fields);
    }
}
