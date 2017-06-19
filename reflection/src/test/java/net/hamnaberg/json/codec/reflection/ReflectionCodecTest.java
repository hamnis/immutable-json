package net.hamnaberg.json.codec.reflection;

import io.vavr.collection.List;
import io.vavr.control.Option;
import net.hamnaberg.json.codec.Codecs;
import net.hamnaberg.json.codec.DecodeResult;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.codec.JsonCodec;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReflectionCodecTest {

    @Test
    public void personReflection() {
        Json.JValue value = Json.jObject(new LinkedHashMap<String, Json.JValue>(){{
            put("name", Json.jString("Erlend Hamnaberg"));
            put("age", Json.jNumber(34));
            put("address", Json.jObject(
                    Json.tuple("street", Json.jString("Ensjøveien")),
                    Json.tuple("city", Json.jString("Oslo"))
            ));
        }});

        JsonCodec<Address> aCodec = new ReflectionCodec<>(Address.class);
        JsonCodec<Person> personCodec = new ReflectionCodec<>(Person.class, Collections.singletonMap("address", aCodec));

        Person person = new Person("Erlend Hamnaberg", 34, new Address("Ensjøveien", "Oslo"));
        DecodeResult<Person> personOpt = personCodec.fromJson(value);
        assertTrue(personOpt.isOk());
        assertEquals(person, personOpt.unsafeGet());
        Json.JValue encoded = personCodec.toJson(person);
        assertEquals(value, encoded);
    }

    @Test
    public void consultantReflectionWithArrays() {
        Json.JValue value = Json.jObject(new LinkedHashMap<String, Json.JValue>() {{
            put("name", Json.jString("Erlend Hamnaberg"));
            put("workplaces", Json.jArray(List.of(
                    Json.jObject(
                            Json.tuple("street", Json.jString("Ensjøveien")),
                            Json.tuple("city", Json.jString("Oslo"))
                    ),
                    Json.jObject(
                            Json.tuple("street", Json.jString("Money, Money, Money")),
                            Json.tuple("city", Json.jString("Oslo"))
                    ))
                    )
            );
        }});

        JsonCodec<Address> aCodec = new ReflectionCodec<>(Address.class);
        Map<String, JsonCodec<?>> codecs = Collections.singletonMap("workplaces", Codecs.listCodec(aCodec));
        JsonCodec<Consultant> consultantCodec = new ReflectionCodec<>(Consultant.class, codecs);
        JsonCodec<Consultant> consultantCodecFactory = new ReflectionCodec<>(Consultant.class, codecs, p -> true, Option.of("create"));

        Consultant consultant = new Consultant("Erlend Hamnaberg", List.of(new Address("Ensjøveien", "Oslo"), new Address("Money, Money, Money", "Oslo")));
        DecodeResult<Consultant> consultantOpt = consultantCodec.fromJson(value);
        DecodeResult<Consultant> consultantFactoryOpt = consultantCodecFactory.fromJson(value);
        assertTrue(consultantOpt.isOk());
        assertEquals(consultant, consultantOpt.unsafeGet());

        assertTrue(consultantFactoryOpt.isOk());
        assertEquals(consultant, consultantFactoryOpt.unsafeGet());

        Json.JValue encoded = consultantCodec.toJson(consultant);
        assertEquals(value, encoded);
    }

    private static class Consultant {
        public final String name;
        public final List<Address> workplaces;

        public Consultant(String name, List<Address> workplaces) {
            this.name = name;
            this.workplaces = workplaces;
        }

        public static Consultant create(String name, List<Address> workplaces) {
            return new Consultant(name, workplaces);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Consultant person = (Consultant) o;

            return name.equals(person.name) && workplaces.equals(person.workplaces);
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode() + workplaces.hashCode();
        }
    }

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
}
