package net.hamnaberg.json.codec.reflection;


import java.util.Optional;

public interface Param {
    Optional<Object> get(Object value);

    String getName();

    Class<?> getType();
}
