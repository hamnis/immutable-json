package net.hamnaberg.json.codec.reflection;

import io.vavr.collection.List;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public interface Factory<A> {
    A invoke(List<Object> params);

    static <A> Factory<A> constructor(Class<A> type, List<Param> params) {
        List<? extends Class<?>> types = params.map(Param::getType);
        try {
            Constructor<A> ctor = type.getConstructor(types.toJavaList().toArray(new Class[params.size()]));
            return params1 -> {
                try {
                    return ctor.newInstance(params1.toJavaArray());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    static <A> Factory<A> factory(Class<A> type, String name, List<Param> params) {
        List<? extends Class<?>> types = params.map(Param::getType);
        try {
            Method method = type.getDeclaredMethod(name, types.toJavaList().toArray(new Class[params.size()]));
            return params1 -> {
                try {
                    return (A)method.invoke(null, params1.toJavaArray());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
