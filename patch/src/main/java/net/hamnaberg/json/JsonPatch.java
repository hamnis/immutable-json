package net.hamnaberg.json;


import net.hamnaberg.json.pointer.JsonPointer;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class JsonPatch implements Function<Json.JValue, Json.JValue> {
    enum Op {
        Add("add"),
        Remove("remove"),
        Replace("replace"),
        Move("move"),
        Copy("copy"),
        Test("test");

        public final String value;

        Op(String value) {
            this.value = value;
        }

        public static Op fromString(String name) {
            for (Op op : values()) {
                if (op.value.equals(name.trim())) {
                    return op;
                }
            }
            throw new IllegalArgumentException(String.format("'%s' is not a legal value for Op", name));
        }
    }

    public static JsonPatch fromArray(Json.JArray array) {
        List<Operation> operations = array.mapToList(j -> Operation.fromJson(j.asJsonObjectOrEmpty()));
        return new JsonPatch(operations);
    }

    private final List<Operation> operations;

    public JsonPatch() {
        this(Collections.emptyList());
    }

    private JsonPatch(List<Operation> operations) {
        this.operations = operations;
    }

    public JsonPatch add(String path, Json.JValue value) {
        return op(Op.Add, Optional.empty(), path, Optional.of(value));
    }

    public JsonPatch remove(String path) {
        return op(Op.Remove, Optional.empty(), path, Optional.empty());
    }

    public JsonPatch replace(String path, Json.JValue value) {
        return op(Op.Replace, Optional.empty(), path, Optional.of(value));
    }

    public JsonPatch test(String path, Json.JValue value) {
        return op(Op.Test, Optional.empty(), path, Optional.of(value));
    }

    public JsonPatch copy(String from, String path) {
        return op(Op.Copy, Optional.of(from), path, Optional.empty());
    }

    public JsonPatch move(String from, String path) {
        return op(Op.Move, Optional.of(from), path, Optional.empty());
    }

    private JsonPatch op(Op op, Optional<String> from, String path, Optional<Json.JValue> value) {
        ArrayList<Operation> list = new ArrayList<>(this.operations);
        list.add(new Operation(op, from, path, value));
        return new JsonPatch(Collections.unmodifiableList(list));
    }

    public Json.JValue apply(final Json.JValue json) {
        Json.JValue modified = json;
        for (Operation op : operations) {
            switch (op.op) {
                case Add:
                    modified = op.path.add(modified, op.value.orElseThrow(() -> new IllegalStateException("Missing value")));
                    break;
                case Remove:
                    modified = op.path.remove(modified);
                    break;
                case Replace:
                    modified = op.path.replace(modified, op.value.orElseThrow(() -> new IllegalStateException("Missing value")));
                    break;
                case Move:
                    modified = op.path.move(json, op.from.orElseThrow(() -> new IllegalStateException("Missing from")));
                    break;
                case Copy:
                    modified = op.path.copy(json, op.from.orElseThrow(() -> new IllegalStateException("Missing from")));
                    break;
                case Test:
                    modified = op.path.test(json, op.value.orElseThrow(() -> new IllegalStateException("Missing value"))) ? modified : modified;
                    break;
            }
        }
        return modified;
    }

    public Json.JArray toJson() {
        return Json.jArray(this.operations.stream().map(Operation::toJson).collect(Collectors.toList()));
    }

}

class Operation {
    public JsonPatch.Op op;
    public Optional<JsonPointer> from;
    public JsonPointer path;
    public Optional<Json.JValue> value;

    public Operation(JsonPatch.Op op, Optional<String> from, String path, Optional<Json.JValue> value) {
        this.op = op;
        this.from = from.map(JsonPointer::compile);
        this.path = JsonPointer.compile(path);
        this.value = value;
    }

    public static Operation fromJson(Json.JObject object) {
        return new Operation(
                JsonPatch.Op.fromString(object.getAsStringOrEmpty("op")),
                object.getAsString("from"),
                object.getAsStringOrEmpty("path"),
                object.get("value")
        );
    }

    public Json.JObject toJson() {
        Map<String, Json.JValue> map = new LinkedHashMap<>();
        map.put("op", Json.jString(op.value));
        from.ifPresent(v -> map.put("from", Json.jString(v.toString())));
        map.put("path", Json.jString(path.toString()));
        value.ifPresent(v -> map.put("value", v));
        return Json.jObject(map);
    }
}
