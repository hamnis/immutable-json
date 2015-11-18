package net.hamnaberg.json.pointer;

import net.hamnaberg.json.Json;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class JsonPointer {
    //This type is wrong, the correct type is Optional<List<Ref>>
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

    @Override
    public String toString() {
        if (path.isEmpty()) return "";
        return path.stream().map(ref -> ref.fold(
                r -> String.valueOf(r.index),
                r -> r.name,
                () -> "-"
        )).collect(Collectors.joining("/", "/", ""));
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

    public Json.JValue add(Json.JValue json, Json.JValue value) {
        if (path.isEmpty()) {
            return value;
        }

        Iterator<Ref> iterator = path.iterator();
        return addImpl(iterator, iterator.next(), json, value);
    }

    public Json.JValue remove(Json.JValue json) {
        if (path.isEmpty()) {
            return json;
        }

        Iterator<Ref> iterator = path.iterator();
        return updateImpl(iterator, iterator.next(), json, Optional.empty());
    }

    public Json.JValue replace(Json.JValue json, Json.JValue value) {
        if (path.isEmpty()) {
            return value;
        }

        Iterator<Ref> iterator = path.iterator();
        return updateImpl(iterator, iterator.next(), json, Optional.of(value));
    }

    public Json.JValue copy(Json.JValue json, JsonPointer from) {
        //TODO: Optimize? we have 3 traversals of the graph
        Optional<Json.JValue> selected = from.select(json);
        return selected.map(v -> add(json, v)).orElse(json);
    }

    public Json.JValue move(Json.JValue json, JsonPointer from) {
        //TODO: Optimize? we have 3 traversals of the graph
        Optional<Json.JValue> selected = from.select(json);
        return selected.map(v -> add(from.remove(json), v)).orElse(json);
    }

    public boolean test(Json.JValue json, Json.JValue value) {
        return select(json).map(value::equals).orElse(false);
    }

    private Json.JValue updateImpl(Iterator<Ref> path, Ref ref, Json.JValue context, Optional<Json.JValue> updateValue) {
        if (ref instanceof ArrayRef && context instanceof Json.JArray) {
            int index = ((ArrayRef) ref).index;
            Json.JArray array = context.asJsonArrayOrEmpty();
            if (!path.hasNext()) {
                if (updateValue.isPresent()) {
                    array.get(index).orElseThrow(() -> new IllegalStateException("No value at index: " + index));
                    return array.replace(index, updateValue.get());
                }
                else {
                    return array.remove(index);
                }
            } else {
                Json.JValue value = array.get(index).orElseThrow(() -> new IllegalStateException("No value at index: " + index));
                return array.replace(index, updateImpl(path, path.next(), value, updateValue));
            }
        }
        else if (ref instanceof PropertyRef && context instanceof Json.JObject) {
            String name = ((PropertyRef) ref).name;
            return replaceObject(path, context, updateValue, name);
        }
        else if (ref instanceof ArrayRef && context instanceof Json.JObject) {
            String name = String.valueOf(((ArrayRef) ref).index);
            return replaceObject(path, context, updateValue, name);
        }
        else if (ref instanceof EndOfArray) {
            throw new IllegalStateException("List index is out-of-bounds");
        }
        return context;
    }

    private Json.JValue replaceObject(Iterator<Ref> path, Json.JValue context, Optional<Json.JValue> updateValue, String name) {
        Json.JObject object = context.asJsonObjectOrEmpty();
        if (!path.hasNext()) {
            if (updateValue.isPresent()) {
                return object.put(name, updateValue.get());
            }
            else {
                return object.remove(name);
            }
        } else {
            Json.JValue value = object.get(name).orElseThrow(() -> new IllegalStateException("No value with name: " + name));
            return object.put(name, updateImpl(path, path.next(), value, updateValue));
        }
    }

    private Json.JValue addImpl(Iterator<Ref> path, Ref ref, Json.JValue context, Json.JValue valueToInsert) {
        if (ref instanceof ArrayRef && context instanceof Json.JArray) {
            int index = ((ArrayRef) ref).index;
            Json.JArray array = context.asJsonArrayOrEmpty();
            if (!path.hasNext()) {
                if (array.size() >= index) {
                    return array.insert(index, valueToInsert);
                }
                else {
                   throw new IllegalStateException(String.format("List index %s is out-of-bounds", index));
                }
            } else {
                Json.JValue value = array.get(index).orElseThrow(() -> new IllegalStateException(String.format("List index %s is out-of-bounds", index)));
                return array.replace(index, addImpl(path, path.next(), value, valueToInsert));
            }
        }
        else if (ref instanceof PropertyRef && context instanceof Json.JObject) {
            String name = ((PropertyRef) ref).name;
            return addObject(path, context, valueToInsert, name);
        }
        else if (ref instanceof ArrayRef && context instanceof Json.JObject) {
            String name = String.valueOf(((ArrayRef) ref).index);
            return addObject(path, context, valueToInsert, name);
        }
        else if (ref instanceof EndOfArray && context instanceof Json.JArray) {
            Json.JArray array = context.asJsonArrayOrEmpty();
            if (path.hasNext()) {
                throw new IllegalStateException("Nonsense to have more values after a end-of-array");
            }
            return array.append(valueToInsert);
        }
        return context;
    }

    private Json.JValue addObject(Iterator<Ref> path, Json.JValue context, Json.JValue valueToInsert, String name) {
        Json.JObject object = context.asJsonObjectOrEmpty();
        if (!path.hasNext()) {
            return object.put(name, valueToInsert);
        } else {
            Json.JValue value = object.get(name).orElseThrow(() -> new IllegalStateException("No value with name: " + name));
            return object.put(name, addImpl(path, path.next(), value, valueToInsert));
        }
    }
}
