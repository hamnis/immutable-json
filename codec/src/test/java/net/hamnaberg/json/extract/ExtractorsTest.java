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
            entry("interests", jArray(jString("Programming"), jString("Books"), jString("podcasts")))
    );

    @Test
    public void extractPerson() {
        TypedField<List<String>> interests = TJArray("interests").mapToOptionalList(JValue::asString);
        Extractor<Person> extractor = Extractors.extract3(
                TString("name"),
                TInt("age"),
                interests,
                Person::new
        );
        Optional<Person> personOpt = extractor.apply(json);
        assertTrue(personOpt.isPresent());
        personOpt.ifPresent(person -> {
            assertEquals("Erlend", person.name);
            assertEquals(35, person.age);
            assertEquals(Arrays.asList("Programming", "Books", "podcasts"), person.interests);
        });
    }


    public static class Person {
        public String name;
        public int age;
        public List<String> interests;

        public Person(String name, int age, List<String> interests) {
            this.name = name;
            this.age = age;
            this.interests = interests;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age='" + age + '\'' +
                    ", interests=" + interests +
                    '}';
        }
    }
}

