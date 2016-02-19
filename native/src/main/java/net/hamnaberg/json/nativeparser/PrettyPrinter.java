package net.hamnaberg.json.nativeparser;

import net.hamnaberg.json.Json;

import java.util.Map;
import java.util.Set;

class PrettyPrinter {
    private final static int INDENT_LEVELS = 16;

    private final int charsPerLevel;
    private final boolean spaceafterColon;
    private final char[] indents;

    private int level = 0;
    private final StringBuilder sb = new StringBuilder();

    PrettyPrinter(int charsPerLevel, boolean spaceAfterColon) {
        this.spaceafterColon = spaceAfterColon;
        this.charsPerLevel = charsPerLevel;
        this.indents = newIndent(charsPerLevel);
    }

    PrettyPrinter(int charsPerLevel) {
        this(charsPerLevel, charsPerLevel > 0);
    }

    public static PrettyPrinter nospaces() {
        return new PrettyPrinter(0);
    }

    public static PrettyPrinter spaces2() {
        return new PrettyPrinter(2);
    }

    public static PrettyPrinter spaces4() {
        return new PrettyPrinter(4);
    }

    private static char[] newIndent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= level; i++) {
            sb.append(" ");
        }
        String indent = sb.toString();

        char[] indents = new char[indent.length() * INDENT_LEVELS];
        int offset = 0;
        for (int i = 0; i < INDENT_LEVELS; i++) {
            indent.getChars(0, indent.length(), indents, offset);
            offset += indent.length();
        }
        return indents;
    }

    private void writeStartObject() {
        sb.append("{");
        this.level++;
    }

    void writeValue(Json.JValue value) {
        value.foldUnit(
                js -> sb.append(toString(js.value)),
                jb -> sb.append(jb.value),
                jn -> sb.append(jn.value.toString()),
                this::writeObject,
                this::writeArray,
                () -> sb.append("null")
        );
    }

    private void writeObject(Json.JObject obj) {
        writeStartObject();

        int index = 0;
        Set<Map.Entry<String, Json.JValue>> set = obj.entrySet();
        for (Map.Entry<String, Json.JValue> entry : set) {
            if (index > 0) {
                sb.append(",");
            }
            doIndent(level);
            writeProperty(entry.getKey(), entry.getValue());
            index++;
        }

        writeEndObject();
    }

    private void writeProperty(String name, Json.JValue value) {
        sb.append(toString(name)).append(":");
        if (spaceafterColon) {
            sb.append(" ");
        }
        writeValue(value);
    }

    private void writeEndObject() {
        this.level--;
        doIndent(this.level);
        sb.append("}");
    }

    private void writeStartArray() {
        sb.append("[");
        this.level++;
    }

    private void writeArray(Json.JArray arr) {
        writeStartArray();

        int length = arr.size();
        for (int i = 0; i < length; i++) {
            Json.JValue v = arr.getValue().get(i);
            if (i > 0) {
                sb.append(",");
            }
            doIndent(level);
            writeValue(v);
        }

        writeEndArray();
    }

    private void writeEndArray() {
        level--;
        doIndent(level);
        sb.append("]");
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    private String toString(String js) {
        StringBuilder sb = new StringBuilder();
        sb.append('\"');

        for (int i = 0; i < js.length(); ++i) {
            char c = js.charAt(i);
            if (c >= 32 && c <= 1114111 && c != 34 && c != 92) {
                sb.append(c);
            } else {
                switch (c) {
                    case '\b':
                        sb.append('\\');
                        sb.append('b');
                        break;
                    case '\t':
                        sb.append('\\');
                        sb.append('t');
                        break;
                    case '\n':
                        sb.append('\\');
                        sb.append('n');
                        break;
                    case '\f':
                        sb.append('\\');
                        sb.append('f');
                        break;
                    case '\r':
                        sb.append('\\');
                        sb.append('r');
                        break;
                    case '\"':
                    case '\\':
                        sb.append('\\');
                        sb.append(c);
                        break;
                    default:
                        String hex = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(hex.substring(hex.length() - 4));
                }
            }
        }

        sb.append('\"');
        return sb.toString();
    }

    private void doIndent(int level) {
        if (charsPerLevel > 0) {
            sb.append("\n");
        }
        if (level > 0) {
            level *= charsPerLevel;
            while (level > indents.length) {
                sb.append(indents, 0, indents.length);
                level -= indents.length;
            }
            sb.append(indents, 0, level);
        }
    }
}
