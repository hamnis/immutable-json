package net.hamnaberg.json;

public enum JsonNull implements JsonValue {

    INSTANCE;

    @Override
    public Type getType() {
        return Type.NULL;
    }
}
