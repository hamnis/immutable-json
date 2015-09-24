package net.hamnaberg.json;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class JsonObject implements JsonValue {
    public final Map<String, JsonValue> value;

    public static JsonObject empty() {
        return new JsonObject(new LinkedHashMap<>());
    }

    public static JsonObject of(Map<String, JsonValue> map) {
        if (map instanceof LinkedHashMap) {
            return new JsonObject((LinkedHashMap<String, JsonValue>) map);
        }
        return of(map.entrySet());
    }

    @SafeVarargs
    public static JsonObject of(Map.Entry<String, JsonValue> first, Map.Entry<String, JsonValue>... rest) {
        return of(JsonArray.listOf(first, rest));
    }

    public static JsonObject of(Iterable<Map.Entry<String, JsonValue>> iterable) {
        return new JsonObject(StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                throwingMerger(),
                LinkedHashMap::new
        )));
    }

    private static <T> BinaryOperator<T> throwingMerger() {
        return (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };
    }

    JsonObject(LinkedHashMap<String, JsonValue> value) {
        this.value = Collections.unmodifiableMap(value);
    }

    @Override
    public Type getType() {
        return Type.OBJECT;
    }

    public Optional<JsonValue> get(String name) {
        return Optional.ofNullable(value.get(name));
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public boolean containsKey(Object key) {
        return value.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.value.containsValue(value);
    }

    public Collection<JsonValue> values() {
        return value.values();
    }

    public Set<Map.Entry<String, JsonValue>> entrySet() {
        return value.entrySet();
    }

    public void forEach(BiConsumer<String, JsonValue> action) {
        value.forEach(action);
    }

    public <B> List<B> mapToList(BiFunction<String, JsonValue, B> f) {
        return entrySet().stream().map(e -> f.apply(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public <B> List<B> mapValues(Function<JsonValue, B> f) {
        return values().stream().map(f).collect(Collectors.toList());
    }

    public JsonValue getOrDefault(Object key, JsonValue defaultValue) {
        return value.getOrDefault(key, defaultValue);
    }

    public int size() {
        return value.size();
    }

    public Set<String> keySet() {
        return value.keySet();
    }

    public Map<String, JsonValue> getValue() {
        return value;
    }

}
