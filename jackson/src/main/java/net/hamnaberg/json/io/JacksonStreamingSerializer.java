package net.hamnaberg.json.io;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import javaslang.Tuple2;
import javaslang.control.Option;
import net.hamnaberg.json.Json;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import java.util.function.Consumer;

public final class JacksonStreamingSerializer implements JsonSerializer {
    private final JsonFactory factory = new JsonFactory();
    private final Option<PrettyPrinter> printer;

    public JacksonStreamingSerializer(Option<PrettyPrinter> printer) {
        this.printer = printer;
    }

    public JacksonStreamingSerializer() {
        this(Option.none());
    }

    public Consumer<OutputStream> toConsumerStream(Json.JValue value) {
        return os -> {
            try {
                write(value, os);
            } catch(RuntimeException | Error e) {
                throw e;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        };
    }

    public void write(Json.JValue value, Writer writer) {
        try (JsonGenerator generator = factory.createGenerator(writer)) {
            printer.forEach(generator::setPrettyPrinter);
            write(value, generator);
        } catch(RuntimeException | Error e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private void write(Json.JValue value, JsonGenerator generator) {
        value.foldUnit(
                liftChecked((j) -> generator.writeString(j.value)),
                liftChecked((j) -> generator.writeBoolean(j.value)),
                liftChecked((j) -> generator.writeNumber(j.value)),
                liftChecked((jObject) -> {
                    generator.writeStartObject();
                    for (Tuple2<String, Json.JValue> property : jObject) {
                        generator.writeFieldName(property._1);
                        write(property._2, generator);
                    }
                    generator.writeEndObject();
                }),
                liftChecked((j) -> {
                    generator.writeStartArray();
                    for (Json.JValue v : j) {
                        write(v, generator);
                    }
                    generator.writeEndArray();
                }),
                liftChecked(generator::writeNull)
        );
    }

    public static <A> Consumer<A> liftChecked(CheckedConsumer<A> f) {
        return (a) -> {
            try {
                f.accept(a);
            } catch(RuntimeException | Error e) {
                throw e;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        };
    }

    public static Runnable liftChecked(CheckedRunnable f) {
        return () -> {
            try {
                f.run();
            } catch(RuntimeException | Error e) {
                throw e;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        };
    }


    private interface CheckedConsumer<A> {
        void accept(A a) throws Exception;
    }

    private interface CheckedRunnable {
        void run() throws Exception;
    }
}
