package net.hamnaberg.json;

import java.math.BigDecimal;
import java.util.Objects;

public final class JsonNumber implements JsonValue {
    public final BigDecimal value;

    public static JsonNumber of(int n) {
        return new JsonNumber(new BigDecimal(n));
    }

    public static JsonNumber of(double n) {
        return new JsonNumber(new BigDecimal(n));
    }

    public static JsonNumber of(long n) {
        return new JsonNumber(new BigDecimal(n));
    }

    public static JsonNumber of(Number n) {
        return new JsonNumber(new BigDecimal(n.toString()));
    }

    public JsonNumber(BigDecimal value) {
        this.value = Objects.requireNonNull(value, "Number may not be null");
    }

    public long asLong() { return value.longValueExact(); }

    public int asInt() { return value.intValueExact(); }

    public double asDouble() { return value.doubleValue(); }

    @Override
    public Type getType() {
        return Type.NUMBER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonNumber that = (JsonNumber) o;

        return value.equals(that.value);
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
