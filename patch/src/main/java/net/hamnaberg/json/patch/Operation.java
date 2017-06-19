package net.hamnaberg.json.patch;

import io.vavr.control.Option;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.pointer.JsonPointer;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

class Operation {
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

    public final Op op;
    public final Option<JsonPointer> from;
    public final JsonPointer path;
    public final Option<Json.JValue> value;

    public Operation(Op op, Option<JsonPointer> from, JsonPointer path, Option<Json.JValue> value) {
        this.op = op;
        this.from = from;
        this.path = path;
        this.value = value;
        if (EnumSet.of(Op.Add, Op.Replace, Op.Test).contains(op)) {
            if (!value.isDefined()) {
                throw new IllegalArgumentException(String.format("Op '%s' requires a value", op.value));
            }
        }
    }

    public static Operation fromJson(Json.JObject object) {
        return new Operation(
                Op.fromString(object.getAsStringOrEmpty("op")),
                object.getAsString("from").map(JsonPointer::compile),
                JsonPointer.compile(object.getAsStringOrEmpty("path")),
                object.get("value")
        );
    }

    public Json.JObject toJson() {
        Map<String, Json.JValue> map = new LinkedHashMap<>();
        map.put("op", Json.jString(op.value));
        from.forEach(v -> map.put("from", Json.jString(v.toString())));
        map.put("path", Json.jString(path.toString()));
        value.forEach(v -> map.put("value", v));
        return Json.jObject(map);
    }
}
