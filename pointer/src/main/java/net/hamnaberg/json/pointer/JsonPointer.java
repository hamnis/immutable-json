package net.hamnaberg.json.pointer;

import net.hamnaberg.json.JsonObject;
import net.hamnaberg.json.JsonValue;

import java.util.*;

public final class JsonPointer {
    private final List<Ref> path;

    public static JsonPointer compile(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            return new JsonPointer(Collections.emptyList());
        }
        return new JsonPointer(new JsonPointerParser().parse(pattern));
    }

    private JsonPointer(List<Ref> path) {
        this.path = path;
    }

    public Optional<JsonValue> select(JsonValue value) {
        if (path.isEmpty()) {
            return Optional.of(value);
        }

        final Iterator<Ref> iterator = path.iterator();
        JsonValue current = value;
        while (iterator.hasNext()) {
            Ref ref = iterator.next();
            if (ref instanceof ArrayRef && current.isArray()) {
                int idx = ((ArrayRef) ref).index;
                List<JsonValue> list = current.asJsonArrayOrEmpty().getValue();
                if (idx < list.size()) {
                    current = list.get(idx);
                }
            } else if (ref instanceof PropertyRef && current.isObject()) {
                String name = ((PropertyRef) ref).name;
                JsonObject object = current.asJsonObjectOrEmpty();
                Optional<JsonValue> maybeValue = object.get(name);
                if (maybeValue.isPresent()) {
                    current = maybeValue.get();
                }
            } else if (ref instanceof EndOfArray) {
                throw new IllegalStateException("List index is out-of-bounds");
            }
            if (!iterator.hasNext() && current != value) {
                return Optional.of(current);
            }
        }

        return Optional.empty();
    }
}
