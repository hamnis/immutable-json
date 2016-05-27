package net.hamnaberg.json.codec.reflection;

import javaslang.control.Option;
import net.hamnaberg.json.Codecs;
import net.hamnaberg.json.DecodeResult;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.JsonCodec;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ReflectionCodec<A> implements JsonCodec<A> {
    private final Class<A> type;
    private final Map<String, JsonCodec<?>> codecs;
    private final List<Param> fields;
    private final Constructor<A> ctor;

    private static Map<Class<?>, JsonCodec<?>> defaultCodecs;
    static {
        Map<Class<?>, JsonCodec<?>> codecs = new HashMap<>();
        codecs.put(String.class, Codecs.StringCodec);
        codecs.put(Integer.class, Codecs.intCodec);
        codecs.put(int.class, Codecs.intCodec);
        codecs.put(double.class, Codecs.doubleCodec);
        codecs.put(long.class, Codecs.longCodec);
        codecs.put(Long.class, Codecs.longCodec);
        codecs.put(Double.class, Codecs.doubleCodec);
        codecs.put(BigInteger.class, Codecs.numberCodec);
        codecs.put(Number.class, Codecs.numberCodec);
        codecs.put(Boolean.class, Codecs.booleanCodec);
        defaultCodecs = Collections.unmodifiableMap(codecs);
    }

    public ReflectionCodec(Class<A> type) {
        this(type, Collections.emptyMap());
    }

    public ReflectionCodec(Class<A> type, Map<String, JsonCodec<?>> codecs) {
        this(type, codecs, (p) -> true);
    }

    public ReflectionCodec(Class<A> type, Map<String, JsonCodec<?>> codecs, Predicate<Param> predicate) {
        this.type = type;
        this.codecs = codecs;
        this.fields = getFields(type).stream().filter(predicate).collect(Collectors.toList());
        this.ctor = getConstructor();
    }

    @Override
    public Option<Json.JValue> toJson(A value) {
        Map<String, Json.JValue> map = new LinkedHashMap<>();
        for (Param field : fields) {
            Option<JsonCodec<Object>> codec = getCodec(field);
            Option<Object> optValue = field.get(value);
            Option<Json.JValue> opt = optValue.flatMap(o -> codec.flatMap(c -> c.toJson(o)));
            opt.forEach(v -> map.put(field.getName(), v));
        }
        return map.isEmpty() ? Option.none() : Option.some(Json.jObject(map));
    }

    @Override
    public DecodeResult<A> fromJson(Json.JValue value) {
        Json.JObject object = value.asJsonObjectOrEmpty();
        List<Object> arguments = new ArrayList<>();

        for (Param field : fields) {
            JsonCodec<Object> codec = getCodec(field).getOrElseThrow(() -> {
                throw new NoSuchElementException("Missing codec for " + field.getName());
            });
            DecodeResult<Object> result = DecodeResult.decode(object, field.getName(), codec);
            result.forEach(arguments::add);
        }

        try {
            return DecodeResult.ok(this.ctor.newInstance(arguments.toArray()));
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

    private Constructor<A> getConstructor() {
        List<Class<?>> args = fields.stream().map(Param::getType).collect(Collectors.toList());
        try {
            return type.getConstructor(args.toArray(new Class[args.size()]));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
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
                fields.add(new FieldParam(field.getName(), field.getType(), field));
            }
        }
        if (fields.isEmpty()) {
            for (Field field : declared) {
                try {
                    Method method = type.getDeclaredMethod(getterOf(field.getName()));
                    if (method != null) {
                        fields.add(new MethodParam(field.getName(), method.getReturnType(), method));
                    }
                } catch (NoSuchMethodException ignore) {
                }
            }
        }
        return fields;
    }


    interface Param {
        Option<Object> get(Object value);
        String getName();
        Class<?> getType();
    }

    static class MethodParam implements Param {
        public final String name;
        public final Class<?> type;
        private final Method method;

        public MethodParam(String name, Class<?> type, Method method) {
            this.name = name;
            this.type = type;
            this.method = method;
        }

        @Override
        public Option<Object> get(Object value) {
            try {
                return Option.of(method.invoke(value));
            } catch (Exception e) {
                return Option.none();
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<?> getType() {
            return type;
        }

        @Override
        public String toString() {
            return "MethodParam{" +
                    "name='" + name + '\'' +
                    ", type=" + type.getName() +
                    '}';
        }
    }

    static class FieldParam implements Param {
        public final String name;
        public final Class<?> type;
        private final Field field;

        public FieldParam(String name, Class<?> type, Field field) {
            this.name = name;
            this.type = type;
            this.field = field;
        }

        public Option<Object> get(Object value) {
            try {
                return Option.of(field.get(value));
            } catch (IllegalAccessException e) {
                return Option.none();
            }
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }

        @Override
        public String toString() {
            return "FieldParam{" +
                    "name='" + name + '\'' +
                    ", type=" + type.getName() +
                    '}';
        }
    }
}
