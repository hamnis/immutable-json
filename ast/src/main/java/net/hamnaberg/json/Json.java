package net.hamnaberg.json;


import java.math.BigDecimal;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javaslang.collection.*;
import javaslang.collection.List;
import javaslang.control.Option;

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
        return new JArray(List.nil());
    }

    public static JArray jArray(Iterable<JValue> iterable) {
        return new JArray(List.ofAll(iterable));
    }

    public static JArray jArray(JValue first, JValue... rest) {
        return new JArray(List.of(rest).prepend(first));
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

    private static <A, B> Function<A, Option<B>> emptyOption() {
        return (ignore) -> Option.none();
    }


    public static abstract class JValue {

        public abstract boolean equals(Object obj);

        public abstract int hashCode();

        /**
         * This is NOT the json representation. For that you
         * will need to use JsonSerializer in the core module.
         *
         * @return as String describing the data structure.
         */
        public abstract String toString();

        public final <X> X fold(Function<JString, X> fString,
                                   Function<JBoolean, X> fBoolean,
                                   Function<JNumber, X> fNumber,
                                   Function<JObject, X> fObject,
                                   Function<JArray, X> fArray,
                                   Supplier<X> fNull) {
            if (this instanceof JString) {
                return fString.apply((JString) this);
            }
            else if (this instanceof JBoolean) {
                return fBoolean.apply((JBoolean)this);
            }
            else if (this instanceof JNumber) {
                return fNumber.apply((JNumber) this);
            }
            else if (this instanceof JObject) {
                return fObject.apply((JObject) this);
            }
            else if (this instanceof JArray) {
                return fArray.apply((JArray) this);
            }
            else if (this instanceof JNull) {
                return fNull.get();
            }
            else {
                throw new RuntimeException("Not a valid AST node");
            }
        }

        public final void foldUnit(Consumer<JString> fString,
                               Consumer<JBoolean> fBoolean,
                               Consumer<JNumber> fNumber,
                               Consumer<JObject> fObject,
                               Consumer<JArray> fArray,
                               Runnable fNull) {
            if (this instanceof JString) {
                fString.accept((JString) this);
            }
            else if (this instanceof JBoolean) {
                fBoolean.accept((JBoolean)this);
            }
            else if (this instanceof JNumber) {
                fNumber.accept((JNumber) this);
            }
            else if (this instanceof JObject) {
                fObject.accept((JObject) this);
            }
            else if (this instanceof JArray) {
                fArray.accept((JArray) this);
            }
            else if (this instanceof JNull) {
                fNull.run();
            }
            else {
                throw new RuntimeException("Not a valid AST node");
            }
        }

        public final Option<JArray> asJsonArray() {
            return fold(Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Option::of, Option::none);
        }

        public final JArray asJsonArrayOrEmpty() {
            return asJsonArray().orElse(jEmptyArray());
        }

        public final Option<JObject> asJsonObject() {
            return fold(Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Option::of, Json.emptyOption(), Option::none);
        }

        public final JObject asJsonObjectOrEmpty() {
            return asJsonObject().orElse(jEmptyObject());
        }

        public final Option<JBoolean> asJsonBoolean() {
            return fold(Json.emptyOption(), Option::of, Json.emptyOption(), Json.emptyOption(), Json.emptyOption(), Option::none);
        }

        public final Option<Boolean> asBoolean() { return asJsonBoolean().map(j -> j.value); }

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

        public long asLong() { return value.longValue(); }

        public int asInt() { return value.intValue(); }

        public double asDouble() { return value.doubleValue(); }

        public BigDecimal getValue() {
            return value;
        }
    }

    public static final class JArray extends JValue implements Iterable<JValue> {
        public Seq<JValue> value;

        private JArray(Seq<JValue> value) {
            this.value = value;
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

        public Seq<JValue> getValue() {
            return value;
        }

        @Override
        public Iterator<JValue> iterator() {
            return value.iterator();
        }

        public Stream<JValue> stream() {
            return StreamSupport.stream(value.spliterator(), false);
        }

        public Option<JValue> get(int index) {
            return index < value.length() ? Option.of(value.get(index)) : Option.none();
        }

        public Option<JValue> headOption() {
            return value.headOption();
        }

        public Seq<JObject> getListAsObjects() {
            return getListAs(JValue::asJsonObject);
        }

        public Seq<String> getListAsStrings() {
            return getListAs(JValue::asString);
        }

        public Seq<BigDecimal> getListAsBigDecimals() {
            return getListAs(JValue::asBigDecimal);
        }

        public <A> Seq<A> getListAs(Function<JValue, Option<A>> f) {
            return value.flatMap(j -> {
                Option<A> opt = f.apply(j);
                return List.ofAll(opt);
            });
        }

        public int size() {
            return value.length();
        }

        public JArray append(JValue toAdd) {
            return new JArray(value.append(toAdd));
        }

        public JArray prepend(JValue toAdd) {
            return new JArray(value.prepend(toAdd));
        }

        public JArray insert(int index, JValue toAdd) {
            return new JArray(value.insert(index, toAdd));
        }

        public JArray remove(int index) {
            return get(index).map(v -> jArray(value.remove(v))).orElse(this);
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

        public Map<String, JValue> getValue() {
            return value;
        }

        public Option<JValue> get(String name) {
            return Option.of(value.get(name));
        }

        public <A> Option<A> getAs(String name, Function<JValue, Option<A>> f) {
            return get(name).flatMap(f);
        }

        public Option<String> getAsString(String name) {
            return getAs(name, JValue::asString);
        }

        public Option<BigDecimal> getAsBigDecimal(String name) {
            return getAs(name, JValue::asBigDecimal);
        }

        public Option<Boolean> getAsBoolean(String name) {
            return getAs(name, JValue::asBoolean);
        }

        public boolean isEmpty() {
            return value.isEmpty();
        }

        public boolean containsKey(Object key) {
            return value.containsKey(key);
        }

        public boolean containsValue(Object value) {
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

        public <B> Seq<B> mapToList(BiFunction<String, JValue, B> f) {
            return entrySet().stream().map(e -> f.apply(e.getKey(), e.getValue())).collect(List.collector());
        }

        public <B> Seq<B> mapValues(Function<JValue, B> f) {
            return values().stream().map(f).collect(List.collector());
        }

        public JValue getOrDefault(Object key, JValue defaultValue) {
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
            Map<String, JValue> map = copyMap();
            map.put(name, value);
            return new JObject(map);
        }

        private Map<String, JValue> copyMap() {
            return new LinkedHashMap<>(value);
        }

    }
}
