package net.hamnaberg.json.codec.reflection;

import java.lang.reflect.Method;
import java.util.Optional;

class MethodParam implements Param {
    public final String name;
    private final Method method;

    public MethodParam(String name, Method method) {
        this.name = name;
        this.method = method;
    }

    @Override
    public Optional<Object> get(Object value) {
        try {
            return Optional.ofNullable(method.invoke(value));
        } catch (Exception e) {
            return Optional.empty();
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
