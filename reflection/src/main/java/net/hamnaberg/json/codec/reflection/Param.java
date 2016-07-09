package net.hamnaberg.json.codec.reflection;

import javaslang.control.Option;

public interface Param {
    Option<Object> get(Object value);

    String getName();

    Class<?> getType();
}
