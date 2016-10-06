package net.hamnaberg.json;

import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.Option;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

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

    private static class Person2 {
        public final String name;
        public final int age;
        public final Option<Address> address;

        public Person2(String name, int age, Option<Address> address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person2 person = (Person2) o;

            if (age != person.age) return false;
            return name.equals(person.name) && address.equals(person.address);
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode() + address.hashCode() + age;
        }


        public Tuple3<String, Integer, Option<Address>> tupled() {
            return new Tuple3<>(name, age, address);
        }
    }

    private enum PersonIso implements Iso<Person, Tuple3<String, Integer, Address>> {
        INSTANCE;

        @Override
        public Person reverseGet(Tuple3<String, Integer, Address> t) {
            return new Person(t._1, t._2, t._3);
        }

        @Override
        public Tuple3<String, Integer, Address> get(Person t) {
            return new Tuple3<>(t.name, t.age, t.address);
        }
    }

    private enum Person2Iso implements Iso<Person2, Tuple3<String, Integer, Option<Address>>> {
        INSTANCE;

        @Override
        public Person2 reverseGet(Tuple3<String, Integer, Option<Address>> t) {
            return new Person2(t._1, t._2, t._3);
        }

        @Override
        public Tuple3<String, Integer, Option<Address>> get(Person2 t) {
            return new Tuple3<>(t.name, t.age, t.address);
        }
    }

    private enum AddressIso implements Iso<Address, Tuple2<String, String>> {
        INSTANCE;

        @Override
        public Address reverseGet(Tuple2<String, String>t) {
            return new Address(t._1, t._2);
        }

        @Override
        public Tuple2<String, String> get(Address t) {
            return new Tuple2<>(t.street, t.city);
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

        JsonCodec<Address> aCodec = Codecs.codec2(AddressIso.INSTANCE, NamedJsonCodec.of("street", Codecs.StringCodec), NamedJsonCodec.of("city", Codecs.StringCodec));
        JsonCodec<Person> personCodec = Codecs.codec3(PersonIso.INSTANCE, NamedJsonCodec.of("name", Codecs.StringCodec), NamedJsonCodec.of("age", Codecs.intCodec), NamedJsonCodec.of("address", aCodec));


        Person person = new Person("Erlend Hamnaberg", 34, new Address("Ensjøveien", "Oslo"));
        DecodeResult<Person> personOpt = personCodec.fromJson(value);
        assertTrue(personOpt.isOk());
        assertEquals(person, personOpt.unsafeGet());
        Option<Json.JValue> jsonOpt = personCodec.toJson(person);
        assertTrue(jsonOpt.isDefined());
        assertEquals(value, jsonOpt.get());
    }

    @Test
    public void person2() {
        Json.JValue value = Json.jObject(new LinkedHashMap<String, Json.JValue>(){{
            put("name", Json.jString("Erlend Hamnaberg"));
            put("age", Json.jNumber(34));
            put("address", Json.jObject(
                    Json.entry("street", Json.jString("Ensjøveien")),
                    Json.entry("city", Json.jString("Oslo"))
            ));
        }});

        Json.JValue optValue = Json.jObject(new LinkedHashMap<String, Json.JValue>(){{
                put("name", Json.jString("Erlend Hamnaberg"));
                put("age", Json.jNumber(34));
        }});
        Json.JValue optValueEqual = optValue.asJsonObjectOrEmpty().put("address", Json.jNull());

        JsonCodec<Address> aCodec = Codecs.codec2(AddressIso.INSTANCE, NamedJsonCodec.of("street", Codecs.StringCodec), NamedJsonCodec.of("city", Codecs.StringCodec));
        JsonCodec<Person2> personCodec = Codecs.codec3(Person2Iso.INSTANCE, NamedJsonCodec.of("name", Codecs.StringCodec), NamedJsonCodec.of("age", Codecs.intCodec), NamedJsonCodec.of("address", Codecs.OptionCodec(aCodec)));

        Person2 person = new Person2("Erlend Hamnaberg", 34, Option.some(new Address("Ensjøveien", "Oslo")));
        Person2 person2 = new Person2("Erlend Hamnaberg", 34, Option.none());

        DecodeResult<Person2> personOpt = personCodec.fromJson(value);
        assertTrue(personOpt.isOk());
        assertEquals(person, personOpt.unsafeGet());

        Option<Json.JValue> jsonOpt = personCodec.toJson(person);
        assertTrue(jsonOpt.isDefined());
        assertEquals(value, jsonOpt.get());

        DecodeResult<Person2> person2Opt = personCodec.fromJson(optValue);
        assertTrue(person2Opt.isOk());
        assertEquals(person2, person2Opt.unsafeGet());

        Option<Json.JValue> json2Opt = personCodec.toJson(person2);
        assertTrue(json2Opt.isDefined());
        assertEquals(optValueEqual, json2Opt.get());

    }

    @Test
    public void personAsTuple() {
        Json.JValue value = Json.jObject(new LinkedHashMap<String, Json.JValue>(){{
            put("name", Json.jString("Erlend Hamnaberg"));
            put("age", Json.jNumber(34));
            put("address", Json.jObject(
                    Json.entry("street", Json.jString("Ensjøveien")),
                    Json.entry("city", Json.jString("Oslo"))
            ));
        }});

        JsonCodec<Address> aCodec = Codecs.codec2(AddressIso.INSTANCE, Codecs.StringCodec.named("street"), Codecs.StringCodec.named("city"));
        JsonCodec<Tuple3<String, Integer, Option<Address>>> personCodec = Codecs.codec3(Iso.identity(), Codecs.StringCodec.named("name"), Codecs.intCodec.named("age"), Codecs.OptionCodec(aCodec).named("address"));

        Person2 person = new Person2("Erlend Hamnaberg", 34, Option.some(new Address("Ensjøveien", "Oslo")));

        DecodeResult<Tuple3<String, Integer, Option<Address>>> personOpt = personCodec.fromJson(value);
        assertTrue(personOpt.isOk());
        assertEquals(person.tupled(), personOpt.unsafeGet());

        Option<Json.JValue> jsonOpt = personCodec.toJson(person.tupled());
        assertTrue(jsonOpt.isDefined());
        assertEquals(value, jsonOpt.get());
    }

    @Test
    public void xmapLocalDateTime() {
        LocalDateTime expected = LocalDateTime.of(1981, 1, 27, 0, 0, 0);
        Json.JString date = Json.jString(expected.format(DateTimeFormatter.ISO_DATE_TIME));

        JsonCodec<LocalDateTime> codec = Codecs.StringCodec.xmap(LocalDateTime::parse, ldt -> ldt.format(DateTimeFormatter.ISO_DATE_TIME));

        DecodeResult<LocalDateTime> localDateTimeOpt = codec.fromJson(date);
        assertTrue(localDateTimeOpt.isOk());

        assertEquals(expected, localDateTimeOpt.unsafeGet());
        assertEquals(date, codec.toJson(expected).get());
    }
}
