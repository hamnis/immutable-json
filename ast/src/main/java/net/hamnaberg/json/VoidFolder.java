package net.hamnaberg.json;

import java.util.function.Consumer;

public interface VoidFolder {
    default void onNull() {
    }

    default void onBoolean(Json.JBoolean b) {
    }

    default void onNumber(Json.JNumber n) {
    }

    default void onString(Json.JString s) {
    }

    default void onArray(Json.JArray a) {
    }

    default void onObject(Json.JObject o) {
    }

    static VoidFolder from(Consumer<Json.JString> fString, Consumer<Json.JBoolean> fBoolean, Consumer<Json.JNumber> fNumber, Consumer<Json.JObject> fObject, Consumer<Json.JArray> fArray, Runnable fNull) {
        return new VoidFolder() {
            @Override
            public void onNull() {
                fNull.run();
            }

            @Override
            public void onBoolean(Json.JBoolean b) {
                fBoolean.accept(b);
            }

            @Override
            public void onNumber(Json.JNumber n) {
                fNumber.accept(n);
            }

            @Override
            public void onString(Json.JString s) {
                fString.accept(s);
            }

            @Override
            public void onArray(Json.JArray a) {
                fArray.accept(a);
            }

            @Override
            public void onObject(Json.JObject o) {
                fObject.accept(o);
            }
        };
    }
}
