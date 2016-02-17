package net.hamnaberg.json.extract;

import javaslang.Function2;
import net.hamnaberg.json.Json;
import static net.hamnaberg.json.extract.TypedField.*;

import java.util.Optional;
import java.util.function.Function;

public abstract class Extractors {
    private Extractors(){}

    public static <TT, A, B> Optional<TT> extract2(Json.JObject json, TypedField<A> f1, TypedField<B> f2, Function2<A, B, TT> func) {
        return json.getAs(f1.name, f1.decoder::fromJson).flatMap(v1 -> json.getAs(f2.name, f2.decoder::fromJson).map(v2 -> func.apply(v1, v2)));
    }

    public static <TT, A, B, C> Function<Json.JObject, Optional<TT>> extract3(TypedField<A> f1, TypedField<B> f2, TypedField<C> f3, javaslang.Function3<A, B, C, TT> func) {
        return (object) -> {
            Optional<A> oa = object.getAs(f1.name, f1.decoder::fromJson);
            Optional<B> ob = object.getAs(f2.name, f2.decoder::fromJson);
            Optional<C> oc = object.getAs(f3.name, f3.decoder::fromJson);
            return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> Optional.of(func.apply(a, b, c)))));
        };
    }


    private static class Person {
        public String name;
        public int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static void main(String[] args) {
        Json.JObject json = Json.jObject(Json.entry("name", Json.jString("Erlend")), Json.entry("age", Json.jNumber(35)));
        Optional<Person> person = extract2(json, TString("name"), TInt("age"), Person::new);
        System.out.println("person = " + person.get());
    }
}
