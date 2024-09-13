package net.hamnaberg.json;

import java.util.Optional;

public class OptionalFolder<A> implements Folder<Optional<A>> {
    @Override
    public Optional<A> onNull() {
        return Optional.empty();
    }

    @Override
    public Optional<A> onBoolean(Json.JBoolean b) {
        return Optional.empty();
    }

    @Override
    public Optional<A> onNumber(Json.JNumber n) {
        return Optional.empty();
    }

    @Override
    public Optional<A> onString(Json.JString s) {
        return Optional.empty();
    }

    @Override
    public Optional<A> onArray(Json.JArray a) {
        return Optional.empty();
    }

    @Override
    public Optional<A> onObject(Json.JObject o) {
        return Optional.empty();
    }
}
