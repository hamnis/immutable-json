package net.hamnaberg.json;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class JsonArray implements JsonValue, Iterable<JsonValue> {
    public final List<JsonValue> value;

    public static JsonArray empty() {
        return new JsonArray(Collections.emptyList());
    }

    public static JsonArray of(String v1, String... rest) {
        return of(listOf(v1, rest), JsonString::new);
    }

    public static JsonArray of(Number v1, Number... rest) {
        return of(listOf(v1, rest), JsonNumber::of);
    }

    public static JsonArray of(JsonValue v1, JsonValue... rest) {
        return new JsonArray(listOf(v1, rest));
    }

    public static <A> JsonArray of(List<A> list, Function<A, JsonValue> f) {
        List<JsonValue> value = list.stream().map(f).collect(Collectors.toList());
        return new JsonArray(value);
    }

    public static JsonArray of(Iterable<JsonValue> iterable) {
        List<JsonValue> list = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
        return new JsonArray(list);
    }

    @SafeVarargs
    public static <A> List<A> listOf(A v1, A... rest) {
        ArrayList<A> list = new ArrayList<>(rest.length + 1);
        list.add(v1);
        list.addAll(Arrays.asList(rest));
        return list;
    }

    public JsonArray(List<JsonValue> value) {
        this.value = Collections.unmodifiableList(Objects.requireNonNull(value, "List may not be null"));
    }

    public Optional<JsonValue> get(int index) {
        return index < value.size() ? Optional.of(value.get(index)) : Optional.empty();
    }

    public List<JsonObject> getListAsObjects() {
        return getListAs(JsonValue::asJsonObject);
    }

    public List<String> getListAsStrings() {
        return getListAs(JsonValue::asString);
    }

    public List<BigDecimal> getListAsBigDecimals() {
        return getListAs(JsonValue::asBigDecimal);
    }

    public <A> List<A> getListAs(Function<JsonValue, Optional<A>> f) {
        return stream().flatMap(j -> {
            Optional<A> obj = f.apply(j);
            return obj.isPresent() ? Stream.of(obj.get()) : Stream.empty();
        }).collect(Collectors.toList());
    }

    @Override
    public Type getType() {
        return Type.ARRAY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonArray jsonArray = (JsonArray) o;

        return value.equals(jsonArray.value);
    }

    public List<JsonValue> getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Iterator<JsonValue> iterator() {
        return value.iterator();
    }

    public Stream<JsonValue> stream() {
        return value.stream();
    }
}
