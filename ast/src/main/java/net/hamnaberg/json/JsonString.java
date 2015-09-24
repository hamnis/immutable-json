package net.hamnaberg.json;

import java.util.Objects;

public final class JsonString implements JsonValue {
    public final String value;

    public static JsonString of(String value) {
        return new JsonString(value);
    }

    public JsonString(String value) {
        this.value = Objects.requireNonNull(value, "String may not be null");
    }

    @Override
    public Type getType() {
        return Type.STRING;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonString that = (JsonString) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public String getValue() {
        return value;
    }
}
