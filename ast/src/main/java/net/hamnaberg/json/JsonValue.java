package net.hamnaberg.json;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

public interface JsonValue {
    enum Type {
        NULL,
        BOOLEAN,
        STRING,
        NUMBER,
        ARRAY,
        OBJECT;
    }

    Type getType();

    default boolean isArray() {
        return getType() == Type.ARRAY;
    }

    default boolean isObject() {
        return getType() == Type.OBJECT;
    }

    default boolean isBoolean() {
        return getType() == Type.BOOLEAN;
    }

    default boolean isNull() {
        return getType() == Type.NULL;
    }

    default boolean isString() {
        return getType() == Type.STRING;
    }

    default boolean isNumber() {
        return getType() == Type.NUMBER;
    }

    default Optional<JsonArray> asJsonArray() {
        return isArray() ? Optional.of((JsonArray)this) : Optional.empty();
    }

    default JsonArray asJsonArrayOrEmpty() {
        return isArray() ? (JsonArray)this : JsonArray.empty();
    }

    default Optional<JsonObject> asJsonObject() {
        return isObject() ? Optional.of((JsonObject)this) : Optional.empty();
    }
    default JsonObject asJsonObjectOrEmpty() {
        return isObject() ? (JsonObject)this : JsonObject.empty();
    }

    default Optional<JsonBoolean> asJsonBoolean() {
        return isBoolean() ? Optional.of((JsonBoolean)this) : Optional.empty();
    }

    default Optional<Boolean> asBoolean() { return asJsonBoolean().map(JsonBoolean::isValue); }

    default Optional<JsonNull> asJsonNull() {
        return isNull() ? Optional.of(JsonNull.INSTANCE) : Optional.empty();
    }

    default Optional<JsonString> asJsonString() {
        return isString() ? Optional.of((JsonString)this) : Optional.empty();
    }

    default Optional<String> asString() {
        return asJsonString().map(JsonString::getValue);
    }

    default Optional<JsonNumber> asJsonNumber() {
        return isNumber() ? Optional.of((JsonNumber)this) : Optional.empty();
    }

    default Optional<BigDecimal> asBigDecimal() {
        return asJsonNumber().map(JsonNumber::getValue);
    }

    default <B> Optional<B> to(Function<JsonValue, Optional<B>> f) {
        return f.apply(this);
    }
}
