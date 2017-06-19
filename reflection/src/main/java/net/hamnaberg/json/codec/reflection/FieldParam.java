package net.hamnaberg.json.codec.reflection;

import io.vavr.control.Option;

import java.lang.reflect.Field;

class FieldParam implements Param {
    private final String name;
    private final Field field;

    public FieldParam(String name, Field field) {
        this.name = name;
        this.field = field;
    }

    public Option<Object> get(Object value) {
        try {
            return Option.of(field.get(value));
        } catch (IllegalAccessException e) {
            return Option.none();
        }
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return field.getType();
    }

    @Override
    public String toString() {
        return "FieldParam{" +
                "name='" + name + '\'' +
                ", type=" + field.getType().getName() +
                '}';
    }
}
