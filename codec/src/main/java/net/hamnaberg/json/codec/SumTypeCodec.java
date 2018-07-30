package net.hamnaberg.json.codec;

import io.vavr.collection.Map;
import net.hamnaberg.json.Json;

import java.util.function.BiPredicate;

public class SumTypeCodec<T> implements JsonCodec<T> {
    private final Class<T> mainType;
    private final BiPredicate<Class<?>, Json.JObject> discriminator;
    private final Map<Class<?>, JsonCodec<? extends T>> subtypes;

    public SumTypeCodec(Class<T> mainType, Map<Class<?>, JsonCodec<? extends T>> subtypes) {
        this(mainType, (type, obj) -> obj.getAsString("type").exists(type.getSimpleName()::equals) ,subtypes);
    }

    public SumTypeCodec(Class<T> type, BiPredicate<Class<?>, Json.JObject> discriminator, Map<Class<?>, JsonCodec<? extends T>> subtypes) {
        this.mainType = type;
        this.discriminator = discriminator;
        this.subtypes = subtypes;
    }

    @Override
    public DecodeResult<T> fromJson(Json.JValue value) {
        Json.JObject object = value.asJsonObjectOrEmpty();
        DecodeResult<? extends T> result = subtypes.find(tuple -> discriminator.test(tuple._1, object)).
                map(t -> t._2.fromJson(object)).getOrElse(() -> DecodeResult.fail("Unable to decode any types"));

        return result.map(it -> it);
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
