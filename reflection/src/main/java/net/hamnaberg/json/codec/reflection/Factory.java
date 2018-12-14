package net.hamnaberg.json.codec.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public interface Factory<A> {
    A invoke(List<Object> params);

    static <A> Factory<A> constructor(Class<A> type, List<Param> params) {
        List<? extends Class<?>> types = params.stream().map(Param::getType).collect(Collectors.toList());
        try {
            Constructor<A> ctor = type.getConstructor(types.toArray(new Class[0]));
            return params1 -> {
                try {
                    return ctor.newInstance(params1.toArray());
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
        List<? extends Class<?>> types = params.stream().map(Param::getType).collect(Collectors.toList());
        try {
            Method method = type.getDeclaredMethod(name, types.toArray(new Class[params.size()]));
            return params1 -> {
                try {
                    return (A)method.invoke(null, params1.toArray());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
