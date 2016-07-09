package net.hamnaberg.json;

import javaslang.control.Option;

public interface NamedJsonCodec<A> extends JsonCodec<A> {
    String name();

    static <A> NamedJsonCodec<A> of(String name, JsonCodec<A> codec) {
        return new DefaultNamedJsonCodec<>(name, codec);
    }
}

class DefaultNamedJsonCodec<A> implements NamedJsonCodec<A> {
    private final String name;
    private final JsonCodec<A> codec;

    DefaultNamedJsonCodec(String name, JsonCodec<A> codec) {
        this.name = name;
        this.codec = codec;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public DecodeResult<A> fromJson(Json.JValue value) {
        return codec.fromJson(value);
    }

    @Override
    public Option<Json.JValue> toJson(A value) {
        return codec.toJson(value);
    }

    @Override
    public Option<A> defaultValue() {
        return codec.defaultValue();
    }

    @Override
    public String toString() {
        return "DefaultNamedJsonCodec{" +
                "name='" + name + '\'' +
                ", codec=" + codec +
                '}';
    }
}
