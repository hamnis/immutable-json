package net.hamnaberg.json.nativeparser;

import net.hamnaberg.json.Json;
import net.hamnaberg.json.io.JsonSerializer;

import java.io.IOException;
import java.io.Writer;
import java.util.stream.Collectors;

public class NativeJsonSerializer implements JsonSerializer {
    private static String INDENT2 = indent(2);
    private static String INDENT4 = indent(4);

    @Override
    public void write(Json.JValue value, Writer writer) {
        try(Writer w = writer) {
            w.write(toString(value));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String toString(Json.JString js) {
        StringBuilder sb = new StringBuilder();
        sb.append('\"');

        for(int i = 0; i < js.value.length(); ++i) {
            char c = js.value.charAt(i);
            if(c >= 32 && c <= 1114111 && c != 34 && c != 92) {
                sb.append(c);
            } else {
                switch(c) {
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

    private String toString(Json.JBoolean jb) {
        return String.valueOf(jb.value);
    }

    private String toString(Json.JArray array) {
        String elements = array.stream().map(this::toString).collect(Collectors.joining(","));
        return "[" + elements + "]";
    }

    private String toString(Json.JObject object) {
        String fields = object.stream()
                .map(e -> String.format("%s:%s", toString(Json.jString(e.getKey())), toString(e.getValue())))
                .collect(Collectors.joining(","));
        return "{" + fields + "}";
    }

    private String toString(Json.JValue value) {
        return value.fold(this::toString, this::toString, this::toString, this::toString, this::toString, () -> "null");
    }


    private static String indent(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= n; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
