package net.hamnaberg.json.codec.reflection;

import io.vavr.control.Option;

import java.lang.reflect.Method;

class MethodParam implements Param {
    public final String name;
    private final Method method;

    public MethodParam(String name, Method method) {
        this.name = name;
        this.method = method;
    }

    @Override
    public Option<Object> get(Object value) {
        try {
            return Option.of(method.invoke(value));
        } catch (Exception e) {
            return Option.none();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return method.getReturnType();
    }

    @Override
    public String toString() {
        return "MethodParam{" +
                "name='" + name + '\'' +
                ", type=" + method.getReturnType().getName() +
                '}';
    }
}
