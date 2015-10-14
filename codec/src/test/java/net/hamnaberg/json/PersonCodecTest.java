package net.hamnaberg.json;

import javaslang.Tuple2;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.*;

public class PersonCodecTest {
    private static class Person {
        public final String name;
        public final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            if (age != person.age) return false;
            return name.equals(person.name);

        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + age;
            return result;
        }
    }

    private enum PersonIso implements Iso<Person, Tuple2<String, Integer>> {
        INSTANCE;

        @Override
        public Function<Tuple2<String, Integer>, Person> reverseGet() {
            return t -> new Person(t._1, t._2);
        }

        @Override
        public Function<Person, Tuple2<String, Integer>> get() {
            return t -> new Tuple2<>(t.name, t.age);
        }
    }


    @Test
    public void personCreate() {
        Json.JValue value = Json.jObject(new LinkedHashMap<String, Json.JValue>(){{
                put("name", Json.jString("Erlend Hamnaberg"));
                put("age", Json.jNumber(34));
        }});

        JsonCodec<Person> codec = Codecs.codec2(PersonIso.INSTANCE, Codecs.StringCodec, Codecs.intCodec).apply("name", "age");


        Person person = new Person("Erlend Hamnaberg", 34);
        Optional<Person> personOpt = codec.fromJson(value);
        assertTrue(personOpt.isPresent());
        assertEquals(person, personOpt.get());
        Optional<Json.JValue> jsonOpt = codec.toJson(person);
        assertTrue(jsonOpt.isPresent());
        assertEquals(value, jsonOpt.get());
    }

    @Test
    public void xmapLocalDateTime() {
        LocalDateTime expected = LocalDateTime.of(1981, 1, 27, 0, 0, 0);
        Json.JString date = Json.jString(expected.format(DateTimeFormatter.ISO_DATE_TIME));

        JsonCodec<LocalDateTime> codec = Codecs.StringCodec.xmap(LocalDateTime::parse, ldt -> ldt.format(DateTimeFormatter.ISO_DATE_TIME));

        Optional<LocalDateTime> localDateTimeOpt = codec.fromJson(date);
        assertTrue(localDateTimeOpt.isPresent());

        assertEquals(expected, localDateTimeOpt.get());
        assertEquals(date, codec.toJson(expected).get());
    }
}
