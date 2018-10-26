package net.hamnaberg.json.codec;

import net.hamnaberg.json.Json;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

public final class SumTypeCodec<T> implements JsonCodec<T> {
    private final Class<T> mainType;
    private final BiPredicate<Class<?>, Json.JObject> discriminator;
    private final Map<Class<?>, JsonCodec<? extends T>> subtypes;

    public SumTypeCodec(Class<T> mainType, Map<Class<?>, JsonCodec<? extends T>> subtypes) {
        this(mainType, c -> c.getSimpleName().toLowerCase(), subtypes);
    }

    public SumTypeCodec(Class<T> mainType, Function<Class<?>, String> nameF, Map<Class<?>, JsonCodec<? extends T>> subtypes) {
        this(mainType, "type", nameF, subtypes);
    }

    public SumTypeCodec(Class<T> mainType, String discriminatorField, Function<Class<?>, String> nameF, Map<Class<?>, JsonCodec<? extends T>> subtypes) {
        this(mainType, (type, obj) -> obj.getAsString(discriminatorField).filter(nameF.apply(type)::equals).isPresent(), subtypes);
    }

    public SumTypeCodec(Class<T> type, BiPredicate<Class<?>, Json.JObject> discriminator, Map<Class<?>, JsonCodec<? extends T>> subtypes) {
        this.mainType = type;
        this.discriminator = discriminator;
        this.subtypes = subtypes;
    }

    @Override
    public DecodeResult<T> fromJson(Json.JValue value) {
        Json.JObject object = value.asJsonObjectOrEmpty();
        DecodeResult<? extends T> result = subtypes.entrySet().stream().filter(tuple -> discriminator.test(tuple.getKey(), object)).findFirst()
                .map(t -> t.getValue().fromJson(object)).orElseGet(() -> DecodeResult.fail("Unable to decode any types"));

        return result.map(it -> it);
    }

    @Override
    public Json.JValue toJson(T value) {
        if (mainType.isInstance(value)) {
            JsonCodec<? extends T> codec = Optional.ofNullable(subtypes.get(value.getClass())).
                    orElseThrow(() -> new IllegalArgumentException(String.format("Not a valid type, %s, available %s", value.getClass(), subtypes.keySet())));
            @SuppressWarnings("unchecked")
            JsonCodec<T> downcast = (JsonCodec<T>)codec;
            return downcast.toJson(value);

        } else {
            throw new IllegalArgumentException(String.format("Not a valid type, %s, available %s", value.getClass(), subtypes.keySet()));
        }
    }
}
