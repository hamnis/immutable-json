package net.hamnaberg.json.pointer;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

interface Ref {

    <A> A fold(Function<ArrayRef,A> arrayF, Function<PropertyRef, A> propertyF, Supplier<A> endOfArrayF);
}

class ArrayRef implements Ref {
    static Pattern pattern = Pattern.compile("0|[1-9][0-9]*");

    public final int index;

    public ArrayRef(int index) {
        this.index = index;
    }

    @Override
    public <A> A fold(Function<ArrayRef, A> arrayF, Function<PropertyRef, A> propertyF, Supplier<A> endOfArrayF) {
        return arrayF.apply(this);
    }
}

enum EndOfArray implements Ref {
    INSTANCE;


    @Override
    public <A> A fold(Function<ArrayRef, A> arrayF, Function<PropertyRef, A> propertyF, Supplier<A> endOfArrayF) {
        return endOfArrayF.get();
    }
}

class PropertyRef implements Ref {
    public final String name;

    public PropertyRef(String name) {
        this.name = name;
    }

    @Override
    public <A> A fold(Function<ArrayRef, A> arrayF, Function<PropertyRef, A> propertyF, Supplier<A> endOfArrayF) {
        return propertyF.apply(this);
    }
}
