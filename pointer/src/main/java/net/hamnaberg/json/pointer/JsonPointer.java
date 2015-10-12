package net.hamnaberg.json.pointer;

import net.hamnaberg.json.Json;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class JsonPointer {
    private final List<Ref> path;

    public static JsonPointer compile(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            return new JsonPointer(Collections.emptyList());
        }
        return new JsonPointer(new JsonPointerParser().parse(pattern));
    }

    private JsonPointer(List<Ref> path) {
        this.path = Collections.unmodifiableList(path);
    }

    public Optional<Json.JValue> select(Json.JValue value) {
        if (path.isEmpty()) {
            return Optional.of(value);
        }

        final Iterator<Ref> iterator = path.iterator();
        Json.JValue current = value;
        while (iterator.hasNext()) {
            Ref ref = iterator.next();
            if (ref instanceof ArrayRef && current instanceof Json.JArray) {
                int idx = ((ArrayRef) ref).index;
                List<Json.JValue> list = current.asJsonArrayOrEmpty().getValue();
                if (idx < list.size()) {
                    current = list.get(idx);
                }
            } else if (ref instanceof PropertyRef && current instanceof Json.JObject) {
                String name = ((PropertyRef) ref).name;
                Json.JObject object = current.asJsonObjectOrEmpty();
                Optional<Json.JValue> maybeValue = object.get(name);
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
