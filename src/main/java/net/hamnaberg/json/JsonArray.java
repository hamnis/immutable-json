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

    public static JsonArray of(JsonValue v1, JsonValue... rest) {
        ArrayList<JsonValue> list = new ArrayList<>(rest.length + 1);
        list.add(v1);
        list.addAll(Arrays.asList(rest));
        return new JsonArray(list);
    }

    public static JsonArray of(Iterable<JsonValue> iterable) {
        List<JsonValue> list = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
        return new JsonArray(list);
    }

    public JsonArray(List<JsonValue> value) {
        this.value = Collections.unmodifiableList(Objects.requireNonNull(value, "List may not be null"));
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
