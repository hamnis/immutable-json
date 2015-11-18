package net.hamnaberg.json.patch;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.hamnaberg.json.Json;

public final class JsonPatch implements Function<Json.JValue, Json.JValue> {

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
        return op(Operation.Op.Add, Optional.empty(), path, Optional.of(value));
    }

    public JsonPatch remove(String path) {
        return op(Operation.Op.Remove, Optional.empty(), path, Optional.empty());
    }

    public JsonPatch replace(String path, Json.JValue value) {
        return op(Operation.Op.Replace, Optional.empty(), path, Optional.of(value));
    }

    public JsonPatch test(String path, Json.JValue value) {
        return op(Operation.Op.Test, Optional.empty(), path, Optional.of(value));
    }

    public JsonPatch copy(String from, String path) {
        return op(Operation.Op.Copy, Optional.of(from), path, Optional.empty());
    }

    public JsonPatch move(String from, String path) {
        return op(Operation.Op.Move, Optional.of(from), path, Optional.empty());
    }

    private JsonPatch op(Operation.Op op, Optional<String> from, String path, Optional<Json.JValue> value) {
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

