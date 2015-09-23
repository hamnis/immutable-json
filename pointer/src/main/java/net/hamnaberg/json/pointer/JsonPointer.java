package net.hamnaberg.json.pointer;

import net.hamnaberg.json.JsonArray;
import net.hamnaberg.json.JsonObject;
import net.hamnaberg.json.JsonValue;

import java.util.*;
import java.util.regex.Pattern;

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
        if (path.isEmpty()) return Optional.of(value);

        final Iterator<Ref> iterator = path.iterator();
        JsonValue current = value;
        while(iterator.hasNext()) {
            Ref ref = iterator.next();
            if (ref instanceof ArrayRef && current.isArray()) {
                int idx = ((ArrayRef) ref).index;
                List<JsonValue> list = value.asJsonArray().orElse(JsonArray.empty()).getValue();
                if (idx < list.size()) {
                    current = list.get(idx);
                }
            }
            else if (ref instanceof PropertyRef && current.isObject()) {
                String name = ((PropertyRef) ref).name;
                JsonObject object = current.asJsonObject().orElse(JsonObject.empty());
                Optional<JsonValue> maybeValue = object.get(name);
                if (maybeValue.isPresent()) {
                    current = maybeValue.get();
                }
            }
            else if (ref instanceof EndOfArray) {
                throw new IllegalStateException("List index is out-of-bounds");
            }
            if (!iterator.hasNext() && current != value) {
                return Optional.of(current);
            }
        }

        return Optional.empty();
    }
}

class JsonPointerParser {
    List<Ref> parse(String s) {
        List<String> list = clean((s.startsWith("/") ? s.substring(1) : s).split("/"));
        return parse(list);
    }

    List<Ref> parse(List<String> parts) {
        List<Ref> path = new ArrayList<>(parts.size());
        for (String p : parts) {
            if (p.equals("-")) {
                path.add(EndOfArray.INSTANCE);
            }
            else if (ArrayRef.pattern.matcher(p).matches()) {
                path.add(new ArrayRef(Integer.parseInt(p)));
            }
            else {
                path.add(new PropertyRef(p));
            }
        }
        return path;
    }

    private List<String> clean(String[] split) {
        ArrayList<String> list = new ArrayList<>(split.length);
        for (String s : split) {
            list.add(unescape(s));
        }
        return list;
    }

    private String unescape(String str) {
        return str.replace("~1", "/").replace("~0", "~");
    }
}

interface Ref {}

class ArrayRef implements Ref {
    static Pattern pattern = Pattern.compile("0|[1-9][0-9]*");

    public final int index;

    public ArrayRef(int index) {
        this.index = index;
    }
}

enum EndOfArray implements Ref {
    INSTANCE;
}

class PropertyRef implements Ref {
    public final String name;

    public PropertyRef(String name) {
        this.name = name;
    }
}
