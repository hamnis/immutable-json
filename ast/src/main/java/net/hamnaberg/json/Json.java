package net.hamnaberg.json;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class Json {
    private Json(){}

    public static JString jString(String value) { return new JString(value); }

    public static JBoolean jBoolean(boolean value) { return new JBoolean(value); }

    public static JNumber jNumber(BigDecimal value) { return new JNumber(value); }

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

    public static JNull jNull() { return JNull.INSTANCE; }

    public static JArray jEmptyArray() {
        return new JArray(Collections.emptyList());
    }

    public static JArray jArray(Iterable<JValue> iterable) {
        List<JValue> list = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
        return new JArray(list);

    }

    public static JArray jArray(JValue first, JValue... rest) {
        ArrayList<JValue> list = new ArrayList<>(Arrays.asList(rest));
        list.add(0, first);
        return new JArray(list);
    }

    public static JObject jEmptyObject() {
        return new JObject(Collections.emptyMap());
    }

    public static JObject jObject(String name, JValue value) {
        return new JObject(Collections.singletonMap(name, value));
    }

    @SafeVarargs
    public static JObject jObject(Map.Entry<String, JValue> first, Map.Entry<String, JValue>... list) {
        LinkedHashMap<String, JValue> map = new LinkedHashMap<>(list.length + 1);
        map.put(first.getKey(), first.getValue());
        for (Map.Entry<String, JValue> entry : list) {
            map.put(entry.getKey(), entry.getValue());
        }
        return new JObject(map);
    }

    public static JObject jObject(Iterable<Map.Entry<String, JValue>> value) {
        LinkedHashMap<String, JValue> map = new LinkedHashMap<>();
        for (Map.Entry<String, JValue> e : value) {
            map.put(e.getKey(), e.getValue());
        }
        return new JObject(map);
    }

    public static JObject jObject(Map<String, JValue> value) {
        LinkedHashMap<String, JValue> map = new LinkedHashMap<>(value);
        return new JObject(map);
    }

    private static <A, B> Function<A, Optional<B>> emptyOption() {
        return (ignore) -> Optional.empty();
    }


    public static abstract class JValue implements Serializable {

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

        public final Optional<Boolean> asBoolean() { return asJsonBoolean().map(j -> j.value); }

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
    }

    public static final class JString extends JValue {
        public final String value;

        private JString(String value) {
            this.value = value;
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
        public BigDecimal value;

        private JNumber(BigDecimal value) {
            this.value = value;
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

        public long asLong() { return value.longValue(); }

        public int asInt() { return value.intValue(); }

        public double asDouble() { return value.doubleValue(); }

        public BigDecimal getValue() {
            return value;
        }
    }

    public static final class JArray extends JValue implements Iterable<JValue> {
        public List<JValue> value;

        private JArray(List<JValue> value) {
            this.value = Collections.unmodifiableList(value);
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
            return StreamSupport.stream(value.spliterator(), false);
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
            Function<JValue, Stream<A>> f2 = f.andThen(opt -> opt.isPresent() ? Stream.of(opt.get()) : Stream.empty());
            return mapStream(f2);
        }

        public <A> List<A> mapToList(Function<JValue, A> f) {
            return value.stream().map(f).collect(Collectors.toList());
        }

        public JArray map(Function<JValue, JValue> f) {
            return new JArray(mapToList(f));
        }

        public JArray flatMap(Function<JValue, JArray> f) {
            Function<JValue, List<JValue>> f2 = f.andThen(JArray::getValue);
            return new JArray(flatMapToList(f2));
        }

        public <A> List<A> flatMapToList(Function<JValue, List<A>> f) {
            return mapStream(f.andThen(List::stream));
        }

        public <A> List<A> mapStream(Function<JValue, Stream<A>> f) {
            return value.stream().flatMap(f).collect(Collectors.toList());
        }

        public int size() {
            return value.size();
        }

        public JArray append(JValue toAdd) {
            ArrayList<JValue> values = new ArrayList<>(value);
            values.add(toAdd);
            return new JArray(values);
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
            ArrayList<JValue> values = new ArrayList<>(value);
            values.add(index, toAdd);
            return new JArray(values);
        }

        public JArray replace(int index, JValue toAdd) {
            ArrayList<JValue> values = new ArrayList<>(value);
            values.set(index, toAdd);
            return new JArray(values);
        }

        public JArray remove(int index) {
            ArrayList<JValue> values = new ArrayList<>(value);
            values.remove(index);
            return new JArray(values);
        }
    }

    public static final class JObject extends JValue implements Iterable<Map.Entry<String, JValue>> {
        public Map<String, JValue> value;

        private JObject(Map<String, JValue> value) {
            this.value = Collections.unmodifiableMap(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JObject jObject = (JObject) o;

            return value.equals(jObject.value);

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

        public Optional<JValue> get(String name) {
            return Optional.ofNullable(value.get(name));
        }

        public <A> Optional<A> getAs(String name, Function<JValue, Optional<A>> f) {
            return get(name).flatMap(f);
        }

        public Optional<String> getAsString(String name) {
            return getAs(name, JValue::asString);
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

        public boolean isEmpty() {
            return value.isEmpty();
        }

        public boolean containsKey(String key) {
            return value.containsKey(key);
        }

        public boolean containsValue(JValue value) {
            return this.value.containsValue(value);
        }

        public Collection<JValue> values() {
            return value.values();
        }

        public Set<Map.Entry<String, JValue>> entrySet() {
            return value.entrySet();
        }

        public void forEach(BiConsumer<String, JValue> action) {
            value.forEach(action);
        }

        public <B> List<B> mapToList(BiFunction<String, JValue, B> f) {
            return entrySet().stream().map(e -> f.apply(e.getKey(), e.getValue())).collect(Collectors.toList());
        }

        public <B> List<B> mapValues(Function<JValue, B> f) {
            return values().stream().map(f).collect(Collectors.toList());
        }

        public JValue getOrDefault(String key, JValue defaultValue) {
            return value.getOrDefault(key, defaultValue);
        }

        public int size() {
            return value.size();
        }

        public Set<String> keySet() {
            return value.keySet();
        }

        @Override
        public Iterator<Map.Entry<String, JValue>> iterator() {
            return entrySet().iterator();
        }

        public Stream<Map.Entry<String, JValue>> stream() {
            return StreamSupport.stream(spliterator(), false);
        }

        public JObject put(String name, JValue value) {
            Map<String, JValue> map = new LinkedHashMap<>(this.value);
            map.put(name, value);
            return new JObject(map);
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

    }
}
