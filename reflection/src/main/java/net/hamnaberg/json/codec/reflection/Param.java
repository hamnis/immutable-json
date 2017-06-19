package net.hamnaberg.json.codec.reflection;

import io.vavr.control.Option;

public interface Param {
    Option<Object> get(Object value);

    String getName();

    Class<?> getType();
}
