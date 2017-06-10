package net.hamnaberg.json;


import io.vavr.Tuple2;
import io.vavr.collection.Map;

public final class PrettyPrinter {
    private final static int INDENT_LEVELS = 16;

    private final int charsPerLevel;
    private final boolean spaceafterColon;
    private final char[] indents;
    private final boolean dropNullKeys;

    public PrettyPrinter(int charsPerLevel, boolean spaceAfterColon, boolean dropNullKeys) {
        this.spaceafterColon = spaceAfterColon;
        this.charsPerLevel = charsPerLevel;
        this.indents = newIndent(charsPerLevel);
        this.dropNullKeys = dropNullKeys;
    }

    public PrettyPrinter(int charsPerLevel) {
        this(charsPerLevel, charsPerLevel > 0, false);
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

    public PrettyPrinter dropNullKeys(boolean choice) {
        return new PrettyPrinter(charsPerLevel, spaceafterColon, choice);
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

    private void writeStartObject(PrinterState state) {
        state.append("{");
        state.levelUp();
    }

    public String writeString(Json.JValue value) {
        PrinterState state = new PrinterState();
        writeValue(value, state);
        return state.toString();
    }

    private void writeValue(Json.JValue value, PrinterState state) {
        value.foldUnit(
                js -> state.append(escape(js.value)),
                jb -> state.append(jb.value),
                jn -> state.append(jn.value.toString()),
                obj -> writeObject(obj, state),
                arr -> writeArray(arr, state),
                () -> state.append("null")
        );
    }

    private void writeObject(Json.JObject obj, PrinterState state) {
        writeStartObject(state);

        int index = 0;
        Map<String, Json.JValue> map = obj.value;
        for (Tuple2<String, Json.JValue> entry : map) {
            if (entry._2.isNull() && dropNullKeys) {
                continue;
            }

            if (index > 0) {
                state.append(",");
            }
            doIndent(state);
            writeProperty(entry._1, entry._2, state);
            index++;
        }

        writeEndObject(state);
    }

    private void writeProperty(String name, Json.JValue value, PrinterState state) {
        state.append(escape(name)).append(":");
        if (spaceafterColon) {
            state.append(" ");
        }
        writeValue(value, state);
    }

    private void writeEndObject(PrinterState state) {
        state.levelDown();
        doIndent(state);
        state.append("}");
    }

    private void writeStartArray(PrinterState state) {
        state.append("[");
        state.levelUp();
    }

    private void writeArray(Json.JArray arr, PrinterState state) {
        writeStartArray(state);

        int length = arr.size();
        for (int i = 0; i < length; i++) {
            Json.JValue v = arr.getValue().get(i);
            if (i > 0) {
                state.append(",");
            }
            doIndent(state);
            writeValue(v, state);
        }

        writeEndArray(state);
    }

    private void writeEndArray(PrinterState state) {
        state.levelDown();
        doIndent(state);
        state.append("]");
    }

    private String escape(String js) {
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

    private void doIndent(PrinterState state) {
        if (charsPerLevel > 0) {
            state.append("\n");
        }
        int level = state.getLevel();
        if (level > 0) {
            level *= charsPerLevel;
            while (level > indents.length) {
                state.append(indents, 0, indents.length);
                level -= indents.length;
            }
            state.append(indents, 0, level);
        }
    }

    private class PrinterState {
        private int level = 0;
        private final StringBuilder sb = new StringBuilder();

        PrinterState append(String s) {
            sb.append(s);
            return this;
        }

        PrinterState append(char[] chars, int i, int length) {
            sb.append(chars, i, length);
            return this;
        }

        PrinterState append(boolean s) {
            sb.append(s);
            return this;
        }

        void levelUp() {
            level++;
        }

        void levelDown() {
            level--;
        }

        int getLevel() {
            return level;
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }
}
