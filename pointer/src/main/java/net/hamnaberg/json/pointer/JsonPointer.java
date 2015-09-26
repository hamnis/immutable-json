package net.hamnaberg.json.pointer;

import javaslang.collection.Seq;
import javaslang.control.Option;
import net.hamnaberg.json.Json;

import java.util.Iterator;

public final class JsonPointer {
    private final Seq<Ref> path;

    public static JsonPointer compile(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            return new JsonPointer(javaslang.collection.List.nil());
        }
        return new JsonPointer(new JsonPointerParser().parse(pattern));
    }

    private JsonPointer(Seq<Ref> path) {
        this.path = path;
    }

    public Option<Json.JValue> select(Json.JValue value) {
        if (path.isEmpty()) {
            return Option.of(value);
        }

        final Iterator<Ref> iterator = path.iterator();
        Json.JValue current = value;
        while (iterator.hasNext()) {
            Ref ref = iterator.next();
            if (ref instanceof ArrayRef && current instanceof Json.JArray) {
                int idx = ((ArrayRef) ref).index;
                Seq<Json.JValue> list = current.asJsonArrayOrEmpty().getValue();
                if (idx < list.length()) {
                    current = list.get(idx);
                }
            } else if (ref instanceof PropertyRef && current instanceof Json.JObject) {
                String name = ((PropertyRef) ref).name;
                Json.JObject object = current.asJsonObjectOrEmpty();
                Option<Json.JValue> maybeValue = object.get(name);
                if (maybeValue.isDefined()) {
                    current = maybeValue.get();
                }
            } else if (ref instanceof EndOfArray) {
                throw new IllegalStateException("List index is out-of-bounds");
            }
            if (!iterator.hasNext() && current != value) {
                return Option.of(current);
            }
        }

        return Option.none();
    }
}
