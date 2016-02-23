package net.hamnaberg.json.extract;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.hamnaberg.json.Json.*;
import static net.hamnaberg.json.extract.TypedField.*;
import static org.junit.Assert.*;

public class ExtractorsTest {

    JObject json = jObject(
            entry("name", jString("Erlend")),
            entry("age", jNumber(35)),
            entry("address", jObject(
               entry("street", jString("Ensjøveien 30 A")),
               entry("city", jString("Oslo")),
               entry("country", jString("Norway"))
            )),
            entry("interests", jArray(jString("Programming"), jString("Books"), jString("podcasts")))
    );

    @Test
    public void extractPerson() {
        Extractor<Address> addressExtractor = Extractors.extract3(
                TString("street"),
                TString("city"),
                TString("country").map(Country::new),
                Address::new
        );

        TypedField<List<String>> interests = TJArray("interests").mapToOptionalList(JValue::asString);
        Extractor<Person> extractor = Extractors.extract4(
                TString("name"),
                TInt("age"),
                TJObject("address").extractTo(addressExtractor),
                interests,
                Person::new
        );
        Optional<Person> personOpt = extractor.apply(json);
        assertTrue(personOpt.isPresent());
        personOpt.ifPresent(person -> {
            assertEquals("Erlend", person.name);
            assertEquals(35, person.age);
            assertEquals("Ensjøveien 30 A", person.address.street);
            assertEquals(Arrays.asList("Programming", "Books", "podcasts"), person.interests);
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

