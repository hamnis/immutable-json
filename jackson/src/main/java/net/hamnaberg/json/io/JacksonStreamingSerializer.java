package net.hamnaberg.json.io;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import javaslang.control.Try;
import net.hamnaberg.json.Json;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import java.util.function.Consumer;

public class JacksonStreamingSerializer implements JsonSerializer {
    private final JsonFactory factory = new JsonFactory();

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
                liftChecked((j) -> {
                    generator.writeStartObject();
                    for (Map.Entry<String, Json.JValue> kv : j) {
                        Json.JValue v = kv.getValue();
                        generator.writeFieldName(kv.getKey());
                        write(v, generator);
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

    public static <A> Consumer<A> liftChecked(Try.CheckedConsumer<A> f) {
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

    public static Runnable liftChecked(Try.CheckedRunnable f) {
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
}
