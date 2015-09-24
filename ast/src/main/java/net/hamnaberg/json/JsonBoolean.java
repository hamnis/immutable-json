package net.hamnaberg.json;

public final class JsonBoolean implements JsonValue {
    public boolean value;

    public JsonBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.BOOLEAN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonBoolean that = (JsonBoolean) o;

        return value == that.value;
    }

    public boolean isValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }
}
