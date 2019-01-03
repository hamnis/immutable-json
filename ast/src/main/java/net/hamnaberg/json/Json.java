package net.hamnaberg.json;


import java.util.Map.Entry;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.Iterator;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        return new JNumber(new BigDecimal(n.toString()));
    }

    public static JNull jNull() {
        return JNull.INSTANCE;
    }

    public static JArray jEmptyArray() {
        return new JArray(List.of());
    }

    public static JArray jArray(Iterable<JValue> iterable) {
        Objects.requireNonNull(iterable, "iterable was null");
        List<JValue> list = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toUnmodifiableList());
        return new JArray(list);
    }

    public static JArray jArray(JValue first, JValue... rest) {
        return new JArray(Stream.concat(Stream.of(first), Stream.of(rest)).collect(Collectors.toUnmodifiableList()));
    }

    public static JObject jEmptyObject() {
        return new JObject(Map.of());
    }

    public static JObject jObject(String name, JValue value) {
        return new JObject(Map.of(
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
        LinkedHashMap<String, JValue> map = new LinkedHashMap<>(Map.of(first.getKey(), first.getValue()));
        for (Entry<String, JValue> kv : list) {
            map.put(kv.getKey(), kv.getValue());
        }
        return new JObject(map);
    }

    public static JObject jObject(Iterable<Entry<String, JValue>> value) {
        if (value instanceof JObject) {
            return (JObject) value;
        }
        return new JObject(copyOf(value));
    }

    public static JObject jObject(
            String k1, JValue v1,
            String k2, JValue v2) {
        return jObject(
                tuple(k1, v1),
                tuple(k2, v2)
        );
    }

    public static JObject jObject(
            String k1, JValue v1,
            String k2, JValue v2,
            String k3, JValue v3) {
        return jObject(
                tuple(k1, v1),
                tuple(k2, v2),
                tuple(k3, v3)
        );
    }

    public static JObject jObject(
            String k1, JValue v1,
            String k2, JValue v2,
            String k3, JValue v3,
            String k4, JValue v4) {
        return jObject(
                tuple(k1, v1),
                tuple(k2, v2),
                tuple(k3, v3),
                tuple(k4, v4)
        );
    }

    public static JObject jObject(
            String k1, JValue v1,
            String k2, JValue v2,
            String k3, JValue v3,
            String k4, JValue v4,
            String k5, JValue v5) {
        return jObject(
                tuple(k1, v1),
                tuple(k2, v2),
                tuple(k3, v3),
                tuple(k4, v4),
                tuple(k5, v5)
        );
    }

    public static JObject jObject(
            String k1, JValue v1,
            String k2, JValue v2,
            String k3, JValue v3,
            String k4, JValue v4,
            String k5, JValue v5,
            String k6, JValue v6) {
        return jObject(
                tuple(k1, v1),
                tuple(k2, v2),
                tuple(k3, v3),
                tuple(k4, v4),
                tuple(k5, v5),
                tuple(k6, v6)
        );
    }

    public static JObject jObject(
            String k1, JValue v1,
            String k2, JValue v2,
            String k3, JValue v3,
            String k4, JValue v4,
            String k5, JValue v5,
            String k6, JValue v6,
            String k7, JValue v7) {
        return jObject(
                tuple(k1, v1),
                tuple(k2, v2),
                tuple(k3, v3),
                tuple(k4, v4),
                tuple(k5, v5),
                tuple(k6, v6),
                tuple(k7, v7)
        );
    }

    public static JObject jObject(
            String k1, JValue v1,
            String k2, JValue v2,
            String k3, JValue v3,
            String k4, JValue v4,
            String k5, JValue v5,
            String k6, JValue v6,
            String k7, JValue v7,
            String k8, JValue v8) {
        return jObject(
                tuple(k1, v1),
                tuple(k2, v2),
                tuple(k3, v3),
                tuple(k4, v4),
                tuple(k5, v5),
                tuple(k6, v6),
                tuple(k7, v7),
                tuple(k8, v8)
        );
    }

    public static JObject jObject(
            String k1, JValue v1,
            String k2, JValue v2,
            String k3, JValue v3,
            String k4, JValue v4,
            String k5, JValue v5,
            String k6, JValue v6,
            String k7, JValue v7,
            String k8, JValue v8,
            String k9, JValue v9) {
        return jObject(
                tuple(k1, v1),
                tuple(k2, v2),
                tuple(k3, v3),
                tuple(k4, v4),
                tuple(k5, v5),
                tuple(k6, v6),
                tuple(k7, v7),
                tuple(k8, v8),
                tuple(k9, v9)
        );
    }

    public static JObject jObject(
            String k1, JValue v1,
            String k2, JValue v2,
            String k3, JValue v3,
            String k4, JValue v4,
            String k5, JValue v5,
            String k6, JValue v6,
            String k7, JValue v7,
            String k8, JValue v8,
            String k9, JValue v9,
            String k10, JValue v10) {
        return jObject(
                tuple(k1, v1),
                tuple(k2, v2),
                tuple(k3, v3),
                tuple(k4, v4),
                tuple(k5, v5),
                tuple(k6, v6),
                tuple(k7, v7),
                tuple(k8, v8),
                tuple(k9, v9),
                tuple(k10, v10)
        );
    }

    public static JObject jObject(Map<String, JValue> value) {
        return jObject(value.entrySet());
    }

    public static Map.Entry<String, JValue> tuple(String name, JValue value) {
        return Map.entry(
                Objects.requireNonNull(name, "Name for entry may not be null"),
                Objects.requireNonNull(value, String.format("Value for named entry '%s' may not be null", name))
        );
    }

    public static Map.Entry<String, JValue> tuple(String name, Optional<JValue> opt) {
        return tuple(name, opt.orElseGet(Json::jNull));
    }

    public static <A> Map.Entry<String, JValue> tuple(String name, A value, Function<A, Optional<JValue>> f) {
        return tuple(name, f.apply(value));
    }

    public static <A> Map.Entry<String, JValue> tuple(String name, Optional<A> value, Function<A, Optional<JValue>> f) {
        return tuple(name, value.flatMap(f));
    }

    public static Map.Entry<String, JValue> tuple(String name, String value) {
        return tuple(name, jString(value));
    }

    public static Map.Entry<String, JValue> tuple(String name, int value) {
        return tuple(name, jNumber(value));
    }

    public static Map.Entry<String, JValue> tuple(String name, double value) {
        return tuple(name, jNumber(value));
    }

    public static Map.Entry<String, JValue> tuple(String name, long value) {
        return tuple(name, jNumber(value));
    }

    public static Map.Entry<String, JValue> tuple(String name, BigDecimal value) {
        return tuple(name, jNumber(value));
    }

    public static Map.Entry<String, JValue> tuple(String name, Number value) {
        return tuple(name, jNumber(value));
    }

    public static Map.Entry<String, JValue> tuple(String name, boolean value) {
        return tuple(name, jBoolean(value));
    }

    public static Map.Entry<String, JValue> nullableTuple(String name, String value) {
        return tuple(name, Optional.ofNullable(value).map(Json::jString));
    }

    public static Map.Entry<String, JValue> nullableTuple(String name, BigDecimal value) {
        return tuple(name, Optional.ofNullable(value).map(Json::jNumber));
    }

    public static Map.Entry<String, JValue> nullableTuple(String name, Number value) {
        return tuple(name, Optional.ofNullable(value).map(Json::jNumber));
    }

    public static Map.Entry<String, JValue> nullableTuple(String name, JValue value) {
        return tuple(name, Optional.ofNullable(value));
    }

    private static LinkedHashMap<String, JValue> copyOf(Iterable<Entry<String, JValue>> value) {
        LinkedHashMap<String, JValue> map = new LinkedHashMap<>();
        for (Entry<String, JValue> kv : value) {
            map.put(kv.getKey(), kv.getValue());
        }
        return map;
    }

    private static <A, B> Function<A, Optional<B>> emptyOption() {
        return (ignore) -> Optional.empty();
    }

    public static abstract class JValue implements Serializable {

        private JValue() {
        }

        public abstract boolean equals(Object obj);

        public abstract int hashCode();

        /**
         * This is NOT the json representation. For that you
         * will need to use {@link #pretty(PrettyPrinter)}.
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

        public final Optional<JArray> asJsonArray() {
            return fold(Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Optional::of, Optional::empty);
        }

        public final JArray asJsonArrayOrEmpty() {
            return asJsonArray().orElse(jEmptyArray());
        }

        public final Optional<JObject> asJsonObject() {
            return fold(Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Optional::of, Json.emptyOption(), Optional::empty);
        }

        public final JObject asJsonObjectOrEmpty() {
            return asJsonObject().orElse(jEmptyObject());
        }

        public final Optional<JBoolean> asJsonBoolean() {
            return fold(Json.emptyOption(), Optional::of, Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Optional::empty);
        }

        public final Optional<Boolean> asBoolean() {
            return asJsonBoolean().map(j -> j.value);
        }

        public final Optional<JNull> asJsonNull() {
            return fold(Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), () -> Optional.of(jNull()));
        }

        public final Optional<JString> asJsonString() {
            return fold(Optional::of, Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Optional::empty);
        }

        public final Optional<String> asString() {
            return asJsonString().map(j -> j.value);
        }

        public final Optional<JNumber> asJsonNumber() {
            return fold(Json.emptyOption(), Json.emptyOption(), Optional::of, Json.emptyOption(), Json.emptyOption(), Optional::empty);
        }

        public final Optional<BigDecimal> asBigDecimal() {
            return asJsonNumber().map(j -> j.value);
        }


        public final boolean isObject() { return asJsonObject().isPresent(); }
        public final boolean isArray() { return asJsonArray().isPresent(); }
        public final boolean isString() { return asJsonString().isPresent(); }
        public final boolean isNull() { return asJsonNull().isPresent(); }
        public final boolean isBoolean() { return asJsonBoolean().isPresent(); }
        public final boolean isNumber() { return asJsonNumber().isPresent(); }

        public final boolean isScalar() {
            return fold(j -> true, j -> true, j -> true, j -> false, j -> false, () -> true);
        }

        public final JValue mapJson(Function<JValue, JValue> f) {
            return f.apply(this);
        }

        public final Optional<String> scalarToString() {
            return fold(
                    j -> Optional.of(j.value),
                    j -> Optional.of(String.valueOf(j.value)),
                    j -> Optional.of(j.value.toString()),
                    emptyOption(),
                    emptyOption(),
                    () -> Optional.of("null")
            );
        }

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
            Optional<JObject> first = asJsonObject();
            Optional<JObject> second = value.asJsonObject();

            if (first.isPresent() && second.isPresent()) {
                return second.get().stream().reduce(first.get(), (obj, kv) -> {
                    Optional<JValue> v1 = obj.get(kv.getKey());
                    if (v1.isPresent()) {
                        return obj.put(kv.getKey(), v1.get().deepmerge(kv.getValue()));
                    } else {
                        return obj.put(kv.getKey(), kv.getValue());
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
            return Objects.equals(value, jString.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
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
            return Objects.hash(value);
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
            return Objects.equals(value, jNumber.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
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
            JArray jValues = (JArray) o;
            return Objects.equals(value, jValues.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
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
            return value.stream();
        }

        public Optional<JValue> get(int index) {
            return index < value.size() ? Optional.of(value.get(index)) : Optional.empty();
        }

        public Optional<JValue> headOption() {
            return value.stream().findFirst();
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

        public <A> List<A> mapOpt(Function<JValue, Optional<A>> f) {
            return this.value.stream().flatMap(f.andThen(Optional::stream)).collect(Collectors.toUnmodifiableList());
        }

        public <A> List<A> mapToList(Function<JValue, A> f) {
            return value.stream().map(f).collect(Collectors.toUnmodifiableList());
        }

        public JArray map(Function<JValue, JValue> f) {
            return new JArray(mapToList(f));
        }

        public JArray flatMap(Function<JValue, JArray> f) {
            Function<JValue, List<JValue>> f2 = f.andThen(JArray::getValue);
            return new JArray(flatMapToList(f2));
        }

        public <A> List<A> flatMapToList(Function<JValue, List<A>> f) {
            return value.stream().flatMap(f.andThen(List::stream)).collect(Collectors.toUnmodifiableList());
        }

        public int size() {
            return value.size();
        }

        public JArray append(JValue toAdd) {
            List<JValue> list = Stream.concat(value.stream(), Stream.of(toAdd)).collect(Collectors.toUnmodifiableList());
            return new JArray(list);
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


        public JArray prepend(JValue toAdd) {
            List<JValue> list = Stream.concat(Stream.of(toAdd), value.stream()).collect(Collectors.toUnmodifiableList());
            return new JArray(list);
        }

        public JArray prepend(String toAdd) {
            return prepend(Json.jString(toAdd));
        }

        public JArray prepend(BigDecimal toAdd) {
            return prepend(Json.jNumber(toAdd));
        }

        public JArray prepend(Number toAdd) {
            return prepend(Json.jNumber(toAdd));
        }

        public JArray prepend(int toAdd) {
            return prepend(Json.jNumber(toAdd));
        }

        public JArray prepend(long toAdd) {
            return prepend(Json.jNumber(toAdd));
        }

        public JArray prepend(double toAdd) {
            return prepend(Json.jNumber(toAdd));
        }

        public JArray prepend(boolean toAdd) {
            return prepend(Json.jBoolean(toAdd));
        }

        public JArray reverse() {
            List<JValue> value = new ArrayList<>(this.value);
            Collections.reverse(value);
            return new JArray(Collections.unmodifiableList(value));
        }

        public JArray insert(int index, JValue toAdd) {
            if (index <= this.value.size()) {
                List<JValue> value = new ArrayList<>(this.value);
                value.add(index, toAdd);
                return new JArray(Collections.unmodifiableList(value));
            }
            return this;
        }

        public JArray replace(int index, JValue toAdd) {
            if (index < this.value.size()) {
                List<JValue> value = new ArrayList<>(this.value);
                value.set(index, toAdd);
                return new JArray(Collections.unmodifiableList(value));
            }
            return this;
        }

        public JArray remove(int index) {
            if (index < this.value.size()) {
                List<JValue> value = new ArrayList<>(this.value);
                value.remove(index);
                return new JArray(Collections.unmodifiableList(value));
            }
            return this;
        }

        public JArray concat(JArray other) {
            return new JArray(Stream.concat(this.value.stream(), other.stream()).collect(Collectors.toUnmodifiableList()));
        }
    }

    public static final class JObject extends JValue implements Iterable<Map.Entry<String, JValue>> {
        public static final Collector<Entry<String, JValue>, ?, Map<String, JValue>> MapCollector = Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue);
        public final Map<String, JValue> value;

        private JObject(Map<String, JValue> value) {
            this.value = Objects.requireNonNull(value, "You may not supply a null Map to JObject");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JObject tuple2s = (JObject) o;
            return Objects.equals(value, tuple2s.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
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

        public Optional<JValue> get(String name) {
            return Optional.ofNullable(value.get(name));
        }

        public <A> Optional<A> getAs(String name, Function<JValue, Optional<A>> f) {
            return get(name).flatMap(f);
        }

        public Optional<String> getAsString(String name) {
            return getAs(name, JValue::asString);
        }

        public String getAsStringOrEmpty(String name) {
            return getAsString(name).orElse("");
        }

        public Optional<JNumber> getAsNumber(String name) {
            return getAs(name, JValue::asJsonNumber);
        }

        public Optional<BigDecimal> getAsBigDecimal(String name) {
            return getAsNumber(name).map(JNumber::getValue);
        }

        public Optional<Integer> getAsInteger(String name) {
            return getAsNumber(name).map(JNumber::asInt);
        }

        public Optional<Double> getAsDouble(String name) {
            return getAsNumber(name).map(JNumber::asDouble);
        }

        public Optional<Long> getAsLong(String name) {
            return getAsNumber(name).map(JNumber::asLong);
        }

        public Optional<Boolean> getAsBoolean(String name) {
            return getAs(name, JValue::asBoolean);
        }

        public Optional<Json.JArray> getAsArray(String name) {
            return getAs(name, JValue::asJsonArray);
        }

        public Json.JArray getAsArrayOrEmpty(String name) {
            return getAsArray(name).orElse(Json.jEmptyArray());
        }

        public Optional<Json.JObject> getAsObject(String name) {
            return getAs(name, JValue::asJsonObject);
        }

        public Json.JObject getAsObjectOrEmpty(String name) {
            return getAsObject(name).orElse(Json.jEmptyObject());
        }

        public Json.JObject filter(BiPredicate<String, JValue> predicate) {
            Map<String, JValue> collect = value.entrySet().stream().filter(e -> predicate.test(e.getKey(), e.getValue())).collect(MapCollector);
            return new Json.JObject(collect);
        }

        public Json.JObject filterKeys(Predicate<String> predicate) {
            Map<String, JValue> collect = value.entrySet().stream().filter(e -> predicate.test(e.getKey())).collect(MapCollector);
            return new Json.JObject(collect);
        }

        public Json.JObject filterNot(BiPredicate<String, JValue> predicate) {
            return filter(predicate.negate());
        }

        public boolean isEmpty() {
            return value.isEmpty();
        }

        public boolean containsKey(String key) {
            return get(key).isPresent();
        }

        public boolean containsValue(JValue value) {
            return this.value.containsValue(value);
        }

        public List<JValue> values() {
            return List.copyOf(value.values());
        }

        public void forEach(BiConsumer<String, JValue> f) {
            value.forEach(f);
        }

        public <B> List<B> mapToList(BiFunction<String, JValue, B> f) {
            return value.entrySet().stream().map(
                    e -> f.apply(e.getKey(), e.getValue())
            ).collect(Collectors.toUnmodifiableList());
        }

        public <B> List<B> mapValues(Function<JValue, B> f) {
            return values().stream().map(f).collect(Collectors.toUnmodifiableList());
        }

        public JValue getOrDefault(String key, JValue defaultValue) {
            return get(key).orElse(defaultValue);
        }

        public int size() {
            return value.size();
        }

        public Set<String> keySet() {
            return value.keySet();
        }

        @Override
        public Iterator<Map.Entry<String, JValue>> iterator() {
            return value.entrySet().iterator();
        }

        public Stream<Map.Entry<String, JValue>> stream() {
            return value.entrySet().stream();
        }

        public JObject put(String name, JValue value) {
            Objects.requireNonNull(name, "Name in JObject.put may not be null");
            Objects.requireNonNull(value, String.format("Value for name %s JObject.put may not be null", name));

            Map<String, JValue> map = new LinkedHashMap<>(this.value);
            map.put(name, value);

            return jObject(map);
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

            Map<String, JValue> copy = new LinkedHashMap<>(this.value);
            copy.putAll(other.value);
            return jObject(copy);
        }

        public JObject remove(String name) {
            if (containsKey(name)) {
                Map<String, JValue> copy = new LinkedHashMap<>(this.value);
                copy.remove(name);
                return jObject(copy);
            }
            return this;
        }
    }
}
