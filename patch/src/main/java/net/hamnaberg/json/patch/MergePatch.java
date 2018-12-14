package net.hamnaberg.json.patch;

import net.hamnaberg.json.Json;
import net.hamnaberg.json.Json.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of Json Merge Patch as defined by <a href="http://tools.ietf.org/html/rfc7396">RFC7396</a>
 */
public abstract class MergePatch {
    private MergePatch() {
    }

    public static JValue diff(JValue obj1, JValue obj2) {
        if (!obj1.isObject() || !obj2.isObject()) {
            return obj2;
        }

        JObject object1 = obj1.asJsonObject().orElse(Json.jEmptyObject());
        JObject object2 = obj2.asJsonObject().orElse(Json.jEmptyObject());

        LinkedHashMap<String, JValue> map = new LinkedHashMap<>();
        object1.forEach((k, v) -> {
            if (object2.containsKey(k)) {
                JValue value = object2.getOrDefault(k, Json.jNull());
                if (!value.equals(v)) {
                    map.put(k, diff(v, value));
                }
            }
            else {
                map.put(k, Json.jNull());
            }
        });

        object2.forEach((k, v) -> {
            if (!object1.containsKey(k)) {
                map.put(k, v);
            }
        });

        return Json.jObject(map);
    }

    public static JValue patch(JValue target, JValue patchValue) {
        Optional<JObject> maybeTarget = target.asJsonObject();
        if (patchValue.asJsonObject().isEmpty()) {
            return patchValue;
        }
        else {
            JObject object = maybeTarget.orElse(Json.jEmptyObject());
            JObject patch = patchValue.asJsonObject().orElse(Json.jEmptyObject());

            Map<String, JValue> map = new LinkedHashMap<>(object.getValue());

            patch.forEach((k, v) -> {
                if (v.asJsonNull().isPresent()) {
                    map.remove(k);
                } else if (map.containsKey(k)) {
                    map.put(k, patch(map.get(k), v));
                } else {
                    map.put(k, patch(Json.jEmptyObject(), v));
                }
            });
            return Json.jObject(map);
        }
    }
}
