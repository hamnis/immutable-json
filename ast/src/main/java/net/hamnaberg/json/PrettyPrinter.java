package net.hamnaberg.json;


import java.io.IOException;
import java.util.Map;

public final class PrettyPrinter {
    private final static int INDENT_LEVELS = 16;

    private final int charsPerLevel;
    private final boolean spaceAfterColon;
    private final boolean dropNullKeys;

    public PrettyPrinter(int charsPerLevel, boolean spaceAfterColon, boolean dropNullKeys) {
        this.spaceAfterColon = spaceAfterColon;
        this.charsPerLevel = charsPerLevel;
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
        return new PrettyPrinter(charsPerLevel, spaceAfterColon, choice);
    }

    public String writeString(Json.JValue value) {
        StringBuilder sb = new StringBuilder();
        writeTo(value, sb);
        return sb.toString();
    }

    public void writeTo(Json.JValue value, Appendable appendable) {
        PrinterState state = new PrinterState(appendable);
        writeValue(value, state);
    }

    private void writeValue(Json.JValue value, PrinterState state) {
        value.foldUnit(new PrinterStateFolder(state, charsPerLevel));
    }

    public static class JsonWriteException extends RuntimeException {
        public JsonWriteException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private class PrinterState {
        private final Appendable appendable;
        private int level = 0;


        private PrinterState(Appendable appendable) {
            this.appendable = appendable;
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

        PrinterState append(String s) {
            try {
                appendable.append(s);
            } catch (IOException e) {
                throw new JsonWriteException("Unable to append to writer", e);
            }
            return this;
        }

        void append(char[] chars, int i, int length) {
            try {
                appendable.append(new String(chars), i, length);
            } catch (IOException e) {
                throw new JsonWriteException("Unable to append to writer", e);
            }
        }

        void append(boolean s) {
            append(String.valueOf(s));
        }
    }

    private class PrinterStateFolder implements VoidFolder {
        private final PrinterState state;
        private final char[] indents;

        private static char[] newIndent(int level) {
            String indent = " ".repeat(Math.max(0, level + 1));

            char[] indents = new char[indent.length() * INDENT_LEVELS];
            int offset = 0;
            for (int i = 0; i < INDENT_LEVELS; i++) {
                indent.getChars(0, indent.length(), indents, offset);
                offset += indent.length();
            }
            return indents;
        }

        public PrinterStateFolder(PrinterState state, int charsPerLevel) {
            this.state = state;
            this.indents = newIndent(charsPerLevel);
        }

        @Override
        public void onNull() {
            state.append("null");
        }

        @Override
        public void onBoolean(Json.JBoolean b) {
            state.append(b.value());
        }

        @Override
        public void onNumber(Json.JNumber n) {
            state.append(n.value().toString());
        }

        @Override
        public void onString(Json.JString s) {
            state.append(escape(s.value()));
        }

        @Override
        public void onArray(Json.JArray a) {
            writeStartArray(state);

            int length = a.size();
            for (int i = 0; i < length; i++) {
                Json.JValue v = a.getValue().get(i);
                if (i > 0) {
                    state.append(",");
                }
                doIndent(state);
                writeValue(v, state);
            }

            writeEndArray(state);
        }

        @Override
        public void onObject(Json.JObject o) {
            writeStartObject(state);

            int index = 0;
            Map<String, Json.JValue> map = o.value();
            for (Map.Entry<String, Json.JValue> entry : map.entrySet()) {
                if (entry.getValue().isNull() && dropNullKeys) {
                    continue;
                }

                if (index > 0) {
                    state.append(",");
                }
                doIndent(state);
                writeProperty(entry.getKey(), entry.getValue(), state);
                index++;
            }

            writeEndObject(state);
        }

        private void writeProperty(String name, Json.JValue value, PrinterState state) {
            state.append(escape(name)).append(":");
            if (spaceAfterColon) {
                state.append(" ");
            }
            writeValue(value, state);
        }

        private void writeStartObject(PrinterState state) {
            state.append("{");
            state.levelUp();
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
    }
}
