package net.hamnaberg.json;


import javaslang.Tuple;
import javaslang.Tuple2;

import javaslang.collection.List;
import javaslang.collection.Set;
import javaslang.collection.LinkedHashMap;
import javaslang.collection.Map;
import java.util.Map.Entry;
import javaslang.control.Option;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.Iterator;
import java.util.function.*;
import java.util.stream.Stream;

public abstract class Json {
    private Json() {
    }

    public static JString jString(String value) {
        return new JString(value);
    }

    public static JBoolean jBoolean(boolean value) {
        return new JBoolean(value);
    }

    public static JNumber jNumber(BigDecimal value) {
        return new JNumber(value);
    }

    public static JNumber jNumber(int n) {
        return new JNumber(new BigDecimal(n));
    }

    public static JNumber jNumber(double n) {
        return new JNumber(new BigDecimal(n));
    }

    public static JNumber jNumber(long n) {
        return new JNumber(new BigDecimal(n));
    }

    public static JNumber jNumber(Number n) {
        if (n instanceof BigDecimal) {
            return new JNumber((BigDecimal) n);
        }
        return new JNumber(new BigDecimal(n.toString()));
    }

    public static JNull jNull() {
        return JNull.INSTANCE;
    }

    public static JArray jEmptyArray() {
        return new JArray(List.empty());
    }

    public static JArray jArray(Iterable<JValue> iterable) {
        return new JArray(List.ofAll(iterable));
    }

    public static JArray jArray(JValue first, JValue... rest) {
        return new JArray(List.of(rest).prepend(first));
    }

    public static JObject jEmptyObject() {
        return new JObject(LinkedHashMap.empty());
    }

    public static JObject jObject(String name, JValue value) {
        return new JObject(LinkedHashMap.of(
                Objects.requireNonNull(name, "Name for entry may not be null"),
                Objects.requireNonNull(value, String.format("Value for named entry '%s' may not be null", name))
        ));
    }

    public static JObject jObject(String name, String value) {
        return jObject(name, jString(value));
    }

    public static JObject jObject(String name, int value) {
        return jObject(name, jNumber(value));
    }

    public static JObject jObject(String name, double value) {
        return jObject(name, jNumber(value));
    }

    public static JObject jObject(String name, long value) {
        return jObject(name, jNumber(value));
    }

    public static JObject jObject(String name, BigDecimal value) {
        return jObject(name, jNumber(value));
    }

    public static JObject jObject(String name, Number value) {
        return jObject(name, jNumber(value));
    }

    public static JObject jObject(String name, boolean value) {
        return jObject(name, jBoolean(value));
    }

    @SafeVarargs
    public static JObject jObject(Entry<String, JValue> first, Entry<String, JValue>... list) {
        return new JObject(LinkedHashMap.of(first.getKey(), first.getValue()).merge(LinkedHashMap.ofEntries(list)));
    }

    @SafeVarargs
    public static JObject jObject(Tuple2<String, JValue> first, Tuple2<String, JValue>... list) {
        return jObject(List.of(list).prepend(first));
    }

    public static JObject jObject(Iterable<Tuple2<String, JValue>> value) {
        if (value instanceof JObject) {
            return (JObject) value;
        }
        return new JObject(LinkedHashMap.ofEntries(value));
    }

    public static JObject jObject(Map<String, JValue> value) {
        if (value instanceof LinkedHashMap) {
            return new JObject(value);
        }
        return new JObject(LinkedHashMap.ofEntries(value));
    }

    public static JObject jObject(java.util.Map<String, JValue> value) {
        return new JObject(LinkedHashMap.ofAll(value));
    }

    public static Tuple2<String, JValue> tuple(String name, JValue value) {
        return Tuple.of(
                Objects.requireNonNull(name, "Name for entry may not be null"),
                Objects.requireNonNull(value, String.format("Value for named entry '%s' may not be null", name))
        );
    }

    public static Tuple2<String, JValue> tuple(String name, Option<JValue> opt) {
        return tuple(name, opt.getOrElse(Json::jNull));
    }

    public static <A> Tuple2<String, JValue> tuple(String name, A value, Function<A, Option<JValue>> f) {
        return tuple(name, f.apply(value));
    }

    public static <A> Tuple2<String, JValue> tuple(String name, Option<A> value, Function<A, Option<JValue>> f) {
        return tuple(name, value.flatMap(f));
    }

    public static Tuple2<String, JValue> tuple(String name, String value) {
        return tuple(name, jString(value));
    }

    public static Tuple2<String, JValue> tuple(String name, int value) {
        return tuple(name, jNumber(value));
    }

    public static Tuple2<String, JValue> tuple(String name, double value) {
        return tuple(name, jNumber(value));
    }

    public static Tuple2<String, JValue> tuple(String name, long value) {
        return tuple(name, jNumber(value));
    }

    public static Tuple2<String, JValue> tuple(String name, BigDecimal value) {
        return tuple(name, jNumber(value));
    }

    public static Tuple2<String, JValue> tuple(String name, Number value) {
        return tuple(name, jNumber(value));
    }

    public static Tuple2<String, JValue> tuple(String name, boolean value) {
        return tuple(name, jBoolean(value));
    }

    private static <A, B> Function<A, Option<B>> emptyOption() {
        return (ignore) -> Option.none();
    }

    public static abstract class JValue implements Serializable {

        private JValue() {
        }

        public abstract boolean equals(Object obj);

        public abstract int hashCode();

        /**
         * This is NOT the json representation. For that you
         * will need to use JsonSerializer in the core module.
         *
         * @return as String describing the data structure.
         */
        public abstract String toString();

        public abstract <X> X fold(Function<JString, X> fString,
                                   Function<JBoolean, X> fBoolean,
                                   Function<JNumber, X> fNumber,
                                   Function<JObject, X> fObject,
                                   Function<JArray, X> fArray,
                                   Supplier<X> fNull);

        public abstract void foldUnit(Consumer<JString> fString,
                                      Consumer<JBoolean> fBoolean,
                                      Consumer<JNumber> fNumber,
                                      Consumer<JObject> fObject,
                                      Consumer<JArray> fArray,
                                      Runnable fNull);

        public final Option<JArray> asJsonArray() {
            return fold(Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Option::of, Option::none);
        }

        public final JArray asJsonArrayOrEmpty() {
            return asJsonArray().getOrElse(jEmptyArray());
        }

        public final Option<JObject> asJsonObject() {
            return fold(Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Option::of, Json.emptyOption(), Option::none);
        }

        public final JObject asJsonObjectOrEmpty() {
            return asJsonObject().getOrElse(jEmptyObject());
        }

        public final Option<JBoolean> asJsonBoolean() {
            return fold(Json.emptyOption(), Option::of, Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Option::none);
        }

        public final Option<Boolean> asBoolean() {
            return asJsonBoolean().map(j -> j.value);
        }

        public final Option<JNull> asJsonNull() {
            return fold(Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), () -> Option.of(jNull()));
        }

        public final Option<JString> asJsonString() {
            return fold(Option::of, Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Option::none);
        }

        public final Option<String> asString() {
            return asJsonString().map(j -> j.value);
        }

        public final Option<JNumber> asJsonNumber() {
            return fold(Json.emptyOption(), Json.emptyOption(), Option::of, Json.emptyOption(), Json.emptyOption(), Option::none);
        }

        public final Option<BigDecimal> asBigDecimal() {
            return asJsonNumber().map(j -> j.value);
        }


        public final boolean isObject() { return asJsonObject().isDefined(); }
        public final boolean isArray() { return asJsonArray().isDefined(); }
        public final boolean isString() { return asJsonString().isDefined(); }
        public final boolean isNull() { return asJsonNull().isDefined(); }
        public final boolean isBoolean() { return asJsonBoolean().isDefined(); }
        public final boolean isNumber() { return asJsonNumber().isDefined(); }

        /**
         * Perform a deep merge of this JSON value with another JSON value.
         * <p>
         * Objects are merged by key, values from the argument JSON take
         * precedence over values from this JSON. Nested objects are
         * recursed.
         * <p>
         * Null, Array, Boolean, String and Number are treated as values,
         * and values from the argument JSON completely replace values
         * from this JSON.
         */
        public final JValue deepmerge(JValue value) {
            Option<JObject> first = asJsonObject();
            Option<JObject> second = value.asJsonObject();

            if (first.isDefined() && second.isDefined()) {
                return second.get().stream().reduce(first.get(), (obj, kv) -> {
                    Option<JValue> v1 = obj.get(kv._1);
                    if (v1.isDefined()) {
                        return obj.put(kv._1, v1.get().deepmerge(kv._2));
                    } else {
                        return obj.put(kv._1, kv._2);
                    }
                }, JObject::concat);
            } else {
                return value;
            }
        }

        public final JValue asJValue() {
            return this;
        }

        public final String nospaces() {
            return pretty(PrettyPrinter.nospaces());
        }

        public final String spaces2() {
            return pretty(PrettyPrinter.spaces2());
        }

        public final String spaces4() {
            return pretty(PrettyPrinter.spaces4());
        }

        public final String pretty(PrettyPrinter p) {
            return p.writeString(this);
        }
    }

    public static final class JString extends JValue {
        public final String value;

        private JString(String value) {
            this.value = Objects.requireNonNull(value, "String may not be null");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JString jString = (JString) o;

            return value.equals(jString.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }


        @Override
        public String toString() {
            return "JString{" +
                    "value='" + value + '\'' +
                    '}';
        }

        @Override
        public <X> X fold(Function<JString, X> fString, Function<JBoolean, X> fBoolean, Function<JNumber, X> fNumber, Function<JObject, X> fObject, Function<JArray, X> fArray, Supplier<X> fNull) {
            return fString.apply(this);
        }

        @Override
        public void foldUnit(Consumer<JString> fString, Consumer<JBoolean> fBoolean, Consumer<JNumber> fNumber, Consumer<JObject> fObject, Consumer<JArray> fArray, Runnable fNull) {
            fString.accept(this);
        }

        public String getValue() {
            return value;
        }
    }

    public static final class JBoolean extends JValue {
        public final boolean value;

        private JBoolean(boolean value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JBoolean jBoolean = (JBoolean) o;

            return value == jBoolean.value;

        }

        @Override
        public int hashCode() {
            return (value ? 1 : 0);
        }

        @Override
        public String toString() {
            return "JBoolean{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public <X> X fold(Function<JString, X> fString, Function<JBoolean, X> fBoolean, Function<JNumber, X> fNumber, Function<JObject, X> fObject, Function<JArray, X> fArray, Supplier<X> fNull) {
            return fBoolean.apply(this);
        }

        @Override
        public void foldUnit(Consumer<JString> fString, Consumer<JBoolean> fBoolean, Consumer<JNumber> fNumber, Consumer<JObject> fObject, Consumer<JArray> fArray, Runnable fNull) {
            fBoolean.accept(this);
        }

        public boolean isValue() {
            return value;
        }
    }

    public static final class JNull extends JValue {
        public static final JNull INSTANCE = new JNull();

        private JNull() {
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return 31;
        }

        @Override
        public String toString() {
            return "JNull";
        }

        @Override
        public <X> X fold(Function<JString, X> fString, Function<JBoolean, X> fBoolean, Function<JNumber, X> fNumber, Function<JObject, X> fObject, Function<JArray, X> fArray, Supplier<X> fNull) {
            return fNull.get();
        }

        @Override
        public void foldUnit(Consumer<JString> fString, Consumer<JBoolean> fBoolean, Consumer<JNumber> fNumber, Consumer<JObject> fObject, Consumer<JArray> fArray, Runnable fNull) {
            fNull.run();
        }
    }

    public static final class JNumber extends JValue {
        public final BigDecimal value;

        private JNumber(BigDecimal value) {
            this.value = Objects.requireNonNull(value, "Number may not be null");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JNumber jNumber = (JNumber) o;

            return value.equals(jNumber.value);

        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "JNumber{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public <X> X fold(Function<JString, X> fString, Function<JBoolean, X> fBoolean, Function<JNumber, X> fNumber, Function<JObject, X> fObject, Function<JArray, X> fArray, Supplier<X> fNull) {
            return fNumber.apply(this);
        }

        @Override
        public void foldUnit(Consumer<JString> fString, Consumer<JBoolean> fBoolean, Consumer<JNumber> fNumber, Consumer<JObject> fObject, Consumer<JArray> fArray, Runnable fNull) {
            fNumber.accept(this);
        }

        public long asLong() {
            return value.longValue();
        }

        public int asInt() {
            return value.intValue();
        }

        public double asDouble() {
            return value.doubleValue();
        }

        public BigDecimal getValue() {
            return value;
        }
    }

    public static final class JArray extends JValue implements Iterable<JValue> {
        public final List<JValue> value;

        private JArray(List<JValue> value) {
            this.value = Objects.requireNonNull(value, "You may not supply a null List in JArray");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JArray jArray = (JArray) o;

            return value.equals(jArray.value);

        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "JArray{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public <X> X fold(Function<JString, X> fString, Function<JBoolean, X> fBoolean, Function<JNumber, X> fNumber, Function<JObject, X> fObject, Function<JArray, X> fArray, Supplier<X> fNull) {
            return fArray.apply(this);
        }

        @Override
        public void foldUnit(Consumer<JString> fString, Consumer<JBoolean> fBoolean, Consumer<JNumber> fNumber, Consumer<JObject> fObject, Consumer<JArray> fArray, Runnable fNull) {
            fArray.accept(this);
        }

        public List<JValue> getValue() {
            return value;
        }

        @Override
        public Iterator<JValue> iterator() {
            return value.iterator();
        }

        public Stream<JValue> stream() {
            return value.toJavaStream();
        }

        public Option<JValue> get(int index) {
            return index < value.size() ? Option.of(value.get(index)) : Option.none();
        }

        public Option<JValue> headOption() {
            return value.headOption();
        }

        public List<JObject> getListAsObjects() {
            return mapOpt(JValue::asJsonObject);
        }

        public List<String> getListAsStrings() {
            return mapOpt(JValue::asString);
        }

        public List<BigDecimal> getListAsBigDecimals() {
            return mapOpt(JValue::asBigDecimal);
        }

        public <A> List<A> mapOpt(Function<JValue, Option<A>> f) {
            return this.value.flatMap(f);
        }

        public <A> List<A> mapToList(Function<JValue, A> f) {
            return value.map(f);
        }

        public JArray map(Function<JValue, JValue> f) {
            return new JArray(mapToList(f));
        }

        public JArray flatMap(Function<JValue, JArray> f) {
            Function<JValue, List<JValue>> f2 = f.andThen(JArray::getValue);
            return new JArray(flatMapToList(f2));
        }

        public <A> List<A> flatMapToList(Function<JValue, List<A>> f) {
            return value.flatMap(f);
        }

        public int size() {
            return value.size();
        }

        public JArray append(JValue toAdd) {
            return new JArray(value.append(toAdd));
        }

        public JArray append(String toAdd) {
            return append(Json.jString(toAdd));
        }

        public JArray append(BigDecimal toAdd) {
            return append(Json.jNumber(toAdd));
        }

        public JArray append(Number toAdd) {
            return append(Json.jNumber(toAdd));
        }

        public JArray append(int toAdd) {
            return append(Json.jNumber(toAdd));
        }

        public JArray append(long toAdd) {
            return append(Json.jNumber(toAdd));
        }

        public JArray append(double toAdd) {
            return append(Json.jNumber(toAdd));
        }

        public JArray append(boolean toAdd) {
            return append(Json.jBoolean(toAdd));
        }

        public JArray insert(int index, JValue toAdd) {
            return new JArray(value.insert(index, toAdd));
        }

        public JArray replace(int index, JValue toAdd) {
            List<JValue> before = value.slice(0, index);
            List<JValue> rest = value.slice(index + 1, value.length());
            List<JValue> added = rest.prepend(toAdd).prependAll(before);
            return new JArray(added);
        }

        public JArray remove(int index) {
            return new JArray(value.removeAt(index));
        }

        public JArray concat(JArray other) {
            return new JArray(other.value.prependAll(this.value));
        }
    }

    public static final class JObject extends JValue implements Iterable<Tuple2<String, JValue>> {
        public final Map<String, JValue> value;

        private JObject(Map<String, JValue> value) {
            this.value = Objects.requireNonNull(value, "You may not supply a null Map to JObject");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JObject jObject = (JObject) o;

            return value.toJavaMap().equals(jObject.value.toJavaMap());

        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "JObject{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public <X> X fold(Function<JString, X> fString, Function<JBoolean, X> fBoolean, Function<JNumber, X> fNumber, Function<JObject, X> fObject, Function<JArray, X> fArray, Supplier<X> fNull) {
            return fObject.apply(this);
        }

        @Override
        public void foldUnit(Consumer<JString> fString, Consumer<JBoolean> fBoolean, Consumer<JNumber> fNumber, Consumer<JObject> fObject, Consumer<JArray> fArray, Runnable fNull) {
            fObject.accept(this);
        }

        public Map<String, JValue> getValue() {
            return value;
        }

        public Option<JValue> get(String name) {
            return value.get(name);
        }

        public <A> Option<A> getAs(String name, Function<JValue, Option<A>> f) {
            return get(name).flatMap(f);
        }

        public Option<String> getAsString(String name) {
            return getAs(name, JValue::asString);
        }

        public String getAsStringOrEmpty(String name) {
            return getAsString(name).getOrElse("");
        }

        public Option<JNumber> getAsNumber(String name) {
            return getAs(name, JValue::asJsonNumber);
        }

        public Option<BigDecimal> getAsBigDecimal(String name) {
            return getAsNumber(name).map(JNumber::getValue);
        }

        public Option<Integer> getAsInteger(String name) {
            return getAsNumber(name).map(JNumber::asInt);
        }

        public Option<Double> getAsDouble(String name) {
            return getAsNumber(name).map(JNumber::asDouble);
        }

        public Option<Long> getAsLong(String name) {
            return getAsNumber(name).map(JNumber::asLong);
        }

        public Option<Boolean> getAsBoolean(String name) {
            return getAs(name, JValue::asBoolean);
        }

        public Option<Json.JArray> getAsArray(String name) {
            return getAs(name, JValue::asJsonArray);
        }

        public Json.JArray getAsArrayOrEmpty(String name) {
            return getAsArray(name).getOrElse(Json.jEmptyArray());
        }

        public Option<Json.JObject> getAsObject(String name) {
            return getAs(name, JValue::asJsonObject);
        }

        public Json.JObject getAsObjectOrEmpty(String name) {
            return getAsObject(name).getOrElse(Json.jEmptyObject());
        }

        public Json.JObject filter(BiPredicate<String, JValue> predicate) {
            return Json.jObject(value.filter(e -> predicate.test(e._1, e._2)));
        }

        public Json.JObject filterKeys(Predicate<String> predicate) {
            return Json.jObject(value.filter(t -> predicate.test(t._1)));
        }

        public Json.JObject filterNot(BiPredicate<String, JValue> predicate) {
            return filter(predicate.negate());
        }

        public boolean isEmpty() {
            return value.isEmpty();
        }

        public boolean containsKey(String key) {
            return get(key).isDefined();
        }

        public boolean containsValue(JValue value) {
            return this.value.containsValue(value);
        }

        public List<JValue> values() {
            return List.ofAll(value.values());
        }

        public void forEach(BiConsumer<String, JValue> f) {
            value.forEach(f);
        }

        public <B> List<B> mapToList(BiFunction<String, JValue, B> f) {
            return value.map(e -> f.apply(e._1, e._2)).toList();
        }

        public <B> List<B> mapValues(Function<JValue, B> f) {
            return values().map(f).toList();
        }

        public JValue getOrDefault(String key, JValue defaultValue) {
            return get(key).getOrElse(defaultValue);
        }

        public int size() {
            return value.size();
        }

        public Set<String> keySet() {
            return value.keySet();
        }

        @Override
        public Iterator<Tuple2<String, JValue>> iterator() {
            return value.iterator();
        }

        public Stream<Tuple2<String, JValue>> stream() {
            return value.toJavaStream();
        }

        public JObject put(String name, JValue value) {
            return new JObject(this.value.put(
                    Objects.requireNonNull(name, "Name in JObject.put may not be null"),
                    Objects.requireNonNull(value, String.format("Value for name %s JObject.put may not be null", name))
            ));
        }

        public JObject put(String name, String value) {
            return put(name, Json.jString(value));
        }

        public JObject put(String name, BigDecimal value) {
            return put(name, Json.jNumber(value));
        }

        public JObject put(String name, Number value) {
            return put(name, Json.jNumber(value));
        }

        public JObject put(String name, int value) {
            return put(name, Json.jNumber(value));
        }

        public JObject put(String name, long value) {
            return put(name, Json.jNumber(value));
        }

        public JObject put(String name, double value) {
            return put(name, Json.jNumber(value));
        }

        public JObject put(String name, boolean value) {
            return put(name, Json.jBoolean(value));
        }

        public JObject concat(JObject other) {
            if (other.isEmpty()) return this;
            if (this == other) return this;

            java.util.Map<String, JValue> copy = this.value.toJavaMap();
            copy.putAll(other.value.toJavaMap());
            return jObject(copy);
        }

        public JObject remove(String name) {
            if (containsKey(name)) {
                return new JObject(this.value.remove(name));
            }
            return this;
        }
    }
}
