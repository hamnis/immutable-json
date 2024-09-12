package net.hamnaberg.json;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Folder<A> {
    A onNull();

    A onBoolean(Json.JBoolean b);

    A onNumber(Json.JNumber n);

    A onString(Json.JString s);

    A onArray(Json.JArray a);

    A onObject(Json.JObject o);

    default VoidFolder toVoid() {
        var self = this;
        return new VoidFolder() {
            @Override
            public void onNull() {
                self.onNull();
            }

            @Override
            public void onBoolean(Json.JBoolean b) {
                self.onBoolean(b);
            }

            @Override
            public void onNumber(Json.JNumber n) {
                self.onNumber(n);
            }

            @Override
            public void onString(Json.JString s) {
                self.onString(s);
            }

            @Override
            public void onArray(Json.JArray a) {
                self.onArray(a);
            }

            @Override
            public void onObject(Json.JObject o) {
                self.onObject(o);
            }
        };
    }

    static <X> Folder<X> from(Function<Json.JString, X> fString, Function<Json.JBoolean, X> fBoolean, Function<Json.JNumber, X> fNumber, Function<Json.JObject, X> fObject, Function<Json.JArray, X> fArray, Supplier<X> fNull) {
        return new Folder<>() {
            @Override
            public X onNull() {
                return fNull.get();
            }

            @Override
            public X onBoolean(Json.JBoolean b) {
                return fBoolean.apply(b);
            }

            @Override
            public X onNumber(Json.JNumber n) {
                return fNumber.apply(n);
            }

            @Override
            public X onString(Json.JString s) {
                return fString.apply(s);
            }

            @Override
            public X onArray(Json.JArray a) {
                return fArray.apply(a);
            }

            @Override
            public X onObject(Json.JObject o) {
                return fObject.apply(o);
            }
        };
    }
}
