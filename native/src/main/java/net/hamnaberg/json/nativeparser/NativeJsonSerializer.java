package net.hamnaberg.json.nativeparser;

import net.hamnaberg.json.Json;
import net.hamnaberg.json.io.JsonSerializer;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Supplier;

public class NativeJsonSerializer implements JsonSerializer {
    private final Supplier<PrettyPrinter> printer;

    public NativeJsonSerializer(Supplier<PrettyPrinter> supplier) {
        printer = supplier;
    }

    public static NativeJsonSerializer nospaces() {
        return new NativeJsonSerializer(PrettyPrinter::nospaces);
    }

    public static NativeJsonSerializer spaces2() {
        return new NativeJsonSerializer(PrettyPrinter::spaces2);
    }

    public static NativeJsonSerializer spaces4() {
        return new NativeJsonSerializer(PrettyPrinter::spaces4);
    }

    @Override
    public void write(Json.JValue value, Writer writer) {
        PrettyPrinter printer = this.printer.get();
        try (Writer w = writer) {
            printer.writeValue(value);
            w.write(printer.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
