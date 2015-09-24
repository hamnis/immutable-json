package net.hamnaberg.json;

public enum JsonBoolean implements JsonValue {
    TRUE(true),
    FALSE(false);

    public boolean value;

    public static JsonBoolean of(boolean value) {
        return value ? TRUE : FALSE;
    }

    JsonBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.BOOLEAN;
    }

    public boolean isValue() {
        return value;
    }
}
