package net.hamnaberg.json;

import javaslang.Tuple2;
import javaslang.Tuple3;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.*;

public class PersonCodecTest {
    private static class Address {
        public final String street;
        public final String city;

        public Address(String street, String city) {
            this.street = street;
            this.city = city;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Address address = (Address) o;

            if (!street.equals(address.street)) return false;
            return city.equals(address.city);

        }

        @Override
        public int hashCode() {
            int result = street.hashCode();
            result = 31 * result + city.hashCode();
            return result;
        }
    }

    private static class Person {
        public final String name;
        public final int age;
        public final Address address;

        public Person(String name, int age, Address address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            if (age != person.age) return false;
            return name.equals(person.name) && address.equals(person.address);
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode() + address.hashCode() + age;
        }
    }

    private enum PersonIso implements Iso<Person, Tuple3<String, Integer, Address>> {
        INSTANCE;

        @Override
        public Function<Tuple3<String, Integer, Address>, Person> reverseGet() {
            return t -> new Person(t._1, t._2, t._3);
        }

        @Override
        public Function<Person, Tuple3<String, Integer, Address>> get() {
            return t -> new Tuple3<>(t.name, t.age, t.address);
        }
    }

    private enum AddressIso implements Iso<Address, Tuple2<String, String>> {
        INSTANCE;

        @Override
        public Function<Tuple2<String, String>, Address> reverseGet() {
            return t -> new Address(t._1, t._2);
        }

        @Override
        public Function<Address, Tuple2<String, String>> get() {
            return t -> new Tuple2<>(t.street, t.city);
        }
    }


    @Test
    public void personCreate() {
        Json.JValue value = Json.jObject(new LinkedHashMap<String, Json.JValue>(){{
                put("name", Json.jString("Erlend Hamnaberg"));
                put("age", Json.jNumber(34));
                put("address", Json.jObject(
                        Json.entry("street", Json.jString("Ensjøveien")),
                        Json.entry("city", Json.jString("Oslo"))
                ));
        }});

        JsonCodec<Address> aCodec = Codecs.codec2(AddressIso.INSTANCE, Codecs.StringCodec, Codecs.StringCodec).apply("street", "city");
        JsonCodec<Person> personCodec = Codecs.codec3(PersonIso.INSTANCE, Codecs.StringCodec, Codecs.intCodec, aCodec).apply("name", "age", "address");


        Person person = new Person("Erlend Hamnaberg", 34, new Address("Ensjøveien", "Oslo"));
        Optional<Person> personOpt = personCodec.fromJson(value);
        assertTrue(personOpt.isPresent());
        assertEquals(person, personOpt.get());
        Optional<Json.JValue> jsonOpt = personCodec.toJson(person);
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
