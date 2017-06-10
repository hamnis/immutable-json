package net.hamnaberg.json.codec;

import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.Test;

import static net.hamnaberg.json.Json.*;
import static net.hamnaberg.json.codec.FieldDecoder.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DecoderTest {

    JObject json = jObject(
            tuple("name", jString("Erlend")),
            tuple("age", jNumber(35)),
            tuple("address", jObject(
                tuple("street", jString("Ensjøveien 30 A")),
                tuple("city", jString("Oslo")),
                tuple("country", jString("Norway"))
            )),
            tuple("interests", jArray(jString("Programming"), jString("Books"), jString("podcasts")))
    );

    JObject json2 = jObject(
            tuple("name", jString("Erlend")),
            tuple("age", jNumber(35))
    );

    @Test
    public void extractPerson() {
        DecodeJson<Address> addressExtractor = Decoders.decode(
                TString("street"),
                TString("city"),
                TString("country").map(Country::new),
                Address::new
        );

        FieldDecoder<List<String>> interests = TJArray("interests").mapToOptionalList(JValue::asString);
        DecodeJson<Person> extractor = Decoders.decode(
                TString("name"),
                TInt("age"),
                TJObject("address").decodeTo(addressExtractor),
                interests,
                Person::new
        );
        DecodeResult<Person> personOpt = extractor.fromJson(json);
        assertTrue(personOpt.isOk());
        personOpt.forEach(person -> {
            assertEquals("Erlend", person.name);
            assertEquals(35, person.age);
            assertEquals("Ensjøveien 30 A", person.address.street);
            assertEquals(List.of("Programming", "Books", "podcasts"), person.interests);
        });
    }

    @Test
    public void extractPerson2() {
        DecodeJson<Address> addressExtractor = Decoders.decode(
                TString("street"),
                TString("city"),
                TString("country").map(Country::new),
                Address::new
        );

        DecodeJson<Person2> extractor = Decoders.decode(
                TString("name"),
                TInt("age"),
                TOptional("address", addressExtractor),
                Person2::new
        );
        DecodeResult<Person2> personOpt = extractor.fromJson(json);
        DecodeResult<Person2> person2Opt = extractor.fromJson(json2);
        assertTrue(personOpt.isOk());
        assertTrue(person2Opt.isOk());
        personOpt.forEach(person -> {
            assertEquals("Erlend", person.name);
            assertEquals(35, person.age);
            assertTrue(person.address.isDefined());
            assertEquals("Ensjøveien 30 A", person.address.get().street);
        });
        person2Opt.forEach(person -> {
            assertEquals("Erlend", person.name);
            assertEquals(35, person.age);
            assertTrue(person.address.isEmpty());
        });
    }


    public static class Person {
        public final String name;
        public final int age;
        public final Address address;
        public final List<String> interests;

        public Person(String name, int age, Address address, List<String> interests) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.interests = interests;
        }


        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", address=" + address +
                    ", interests=" + interests +
                    '}';
        }
    }

    public static class Person2 {
        public final String name;
        public final int age;
        public final Option<Address> address;

        public Person2(String name, int age, Option<Address> address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }


        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", address=" + address +
                    '}';
        }
    }

    public static class Country {
        public final String name;

        public Country(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public static class Address {
        public final String street;
        public final String city;
        public final Country country;

        public Address(String street, String city, Country country) {
            this.street = street;
            this.city = city;
            this.country = country;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "street='" + street + '\'' +
                    ", city='" + city + '\'' +
                    ", country=" + country +
                    '}';
        }
    }
}

