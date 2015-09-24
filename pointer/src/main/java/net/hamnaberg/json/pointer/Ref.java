package net.hamnaberg.json.pointer;

import java.util.regex.Pattern;

interface Ref {}

class ArrayRef implements Ref {
    static Pattern pattern = Pattern.compile("0|[1-9][0-9]*");

    public final int index;

    public ArrayRef(int index) {
        this.index = index;
    }
}

enum EndOfArray implements Ref {
    INSTANCE;
}

class PropertyRef implements Ref {
    public final String name;

    public PropertyRef(String name) {
        this.name = name;
    }
}
