package net.hamnaberg.json;

import java.util.Optional;
import java.util.function.Function;

public interface JsonCodec<A> extends EncodeJson<A>, DecodeJson<A> {
    default <B> JsonCodec<B> xmap(Function<A, B> f1, Function<B, A> f2) {
        JsonCodec<A> that = this;
        return new JsonCodec<B>() {
            @Override
            public Optional<B> fromJson(Json.JValue value) {
                return that.fromJson(value).map(f1);
            }

            @Override
            public Optional<Json.JValue> toJson(B value) {
                return that.toJson(f2.apply(value));
            }
        };
    }
}
