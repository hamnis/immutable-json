package net.hamnaberg.json.patch;


import java.util.function.Function;

import io.vavr.collection.List;
import io.vavr.control.Option;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.pointer.JsonPointer;

public final class JsonPatch implements Function<Json.JValue, Json.JValue> {

    public static JsonPatch fromArray(Json.JArray array) {
        List<Operation> operations = array.mapToList(j -> Operation.fromJson(j.asJsonObjectOrEmpty()));
        return new JsonPatch(operations);
    }

    private final List<Operation> operations;

    public JsonPatch() {
        this(List.empty());
    }

    private JsonPatch(List<Operation> operations) {
        this.operations = operations;
    }

    public JsonPatch add(String path, Json.JValue value) {
        return add(JsonPointer.compile(path), value);
    }

    public JsonPatch add(JsonPointer path, Json.JValue value) {
        return op(Operation.Op.Add, Option.none(), path, Option.of(value));
    }

    public JsonPatch remove(String path) {
        return remove(JsonPointer.compile(path));
    }

    public JsonPatch remove(JsonPointer path) {
        return op(Operation.Op.Remove, Option.none(), path, Option.none());
    }

    public JsonPatch replace(String path, Json.JValue value) {
        return replace(JsonPointer.compile(path), value);
    }

    public JsonPatch replace(JsonPointer path, Json.JValue value) {
        return op(Operation.Op.Replace, Option.none(), path, Option.of(value));
    }

    public JsonPatch test(String path, Json.JValue value) {
        return test(JsonPointer.compile(path), value);
    }

    public JsonPatch test(JsonPointer path, Json.JValue value) {
        return op(Operation.Op.Test, Option.none(), path, Option.of(value));
    }

    public JsonPatch copy(String from, String path) {
        return copy(JsonPointer.compile(from), JsonPointer.compile(path));
    }

    public JsonPatch copy(JsonPointer from, JsonPointer path) {
        return op(Operation.Op.Copy, Option.of(from), path, Option.none());
    }

    public JsonPatch move(String from, String path) {
        return move(JsonPointer.compile(from), JsonPointer.compile(path));
    }

    public JsonPatch move(JsonPointer from, JsonPointer path) {
        return op(Operation.Op.Move, Option.of(from), path, Option.none());
    }

    private JsonPatch op(Operation.Op op, Option<JsonPointer> from, JsonPointer path, Option<Json.JValue> value) {
        return new JsonPatch(this.operations.append(new Operation(op, from, path, value)));
    }

    public Json.JValue apply(final Json.JValue json) {
        Json.JValue modified = json;
        for (Operation op : operations) {
            switch (op.op) {
                case Add:
                    modified = op.path.add(modified, op.value.getOrElseThrow(() -> new IllegalStateException("Missing value")));
                    break;
                case Remove:
                    modified = op.path.remove(modified);
                    break;
                case Replace:
                    modified = op.path.replace(modified, op.value.getOrElseThrow(() -> new IllegalStateException("Missing value")));
                    break;
                case Move:
                    modified = op.path.move(json, op.from.getOrElseThrow(() -> new IllegalStateException("Missing from")));
                    break;
                case Copy:
                    modified = op.path.copy(json, op.from.getOrElseThrow(() -> new IllegalStateException("Missing from")));
                    break;
                case Test:
                    modified = op.path.test(json, op.value.getOrElseThrow(() -> new IllegalStateException("Missing value"))) ? modified : modified;
                    break;
            }
        }
        return modified;
    }

    public Json.JArray toJson() {
        return Json.jArray(this.operations.map(Operation::toJson));
    }
}

