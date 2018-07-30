package net.hamnaberg.json.codec;

import io.vavr.collection.Map;
import net.hamnaberg.json.Json;

public class ADTCodec<T> implements JsonCodec<T> {
    private final Class<T> mainType;
    private final Map<Class<?>, JsonCodec<? extends T>> subtypes;

    public ADTCodec(Class<T> type, Map<Class<?>, JsonCodec<? extends T>> subtypes) {
        mainType = type;
        this.subtypes = subtypes;
    }

    @Override
    public DecodeResult<T> fromJson(Json.JValue value) {
        DecodeJson<T> codec = subtypes.values().foldLeft(
                DecodeJson.failure("Unable to decode any types:"),
                (c1, c2) -> c1.map(it -> it).or(c2.map(it -> (T) it))
        );
        return codec.fromJson(value);
    }

    @Override
    public Json.JValue toJson(T value) {
        if (mainType.isInstance(value)) {
            JsonCodec<? extends T> codec = subtypes.
                    get(value.getClass()).
                    getOrElseThrow(() -> new IllegalArgumentException(String.format("Not a valid type, %s, available %s", value.getClass(), subtypes.keySet())));
            @SuppressWarnings("unchecked")
            JsonCodec<T> downcast = (JsonCodec<T>)codec;
            return downcast.toJson(value);

        } else {
            throw new IllegalArgumentException(String.format("Not a valid type, %s, available %s", value.getClass(), subtypes.keySet()));
        }
    }
}
