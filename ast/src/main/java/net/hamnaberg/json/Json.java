package net.hamnaberg.json;


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
        List<JValue> list = Stream.concat(Stream.of(first), Stream.of(rest)).collect(Collectors.toList());
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


    public static abstract class JValue {

        public abstract boolean equals(Object obj);

        public abstract int hashCode();

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
            /*else {
                throw new RuntimeException("Not a valid AST node");
            }*/
        }

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

        public List<JValue> getValue() {
            return value;
        }

        public Optional<JValue> get(int index) {
            return index < value.size() ? Optional.of(value.get(index)) : Optional.empty();
        }

        public Optional<JValue> headOption() {
            return stream().findFirst();
        }

        public List<JObject> getListAsObjects() {
            return getListAs(JValue::asJsonObject);
        }

        public List<String> getListAsStrings() {
            return getListAs(JValue::asString);
        }

        public List<BigDecimal> getListAsBigDecimals() {
            return getListAs(JValue::asBigDecimal);
        }

        public <A> List<A> getListAs(Function<JValue, Optional<A>> f) {
            return stream().flatMap(j -> {
                Optional<A> obj = f.apply(j);
                return obj.isPresent() ? Stream.of(obj.get()) : Stream.empty();
            }).collect(Collectors.toList());
        }


        @Override
        public Iterator<JValue> iterator() {
            return value.iterator();
        }

        public Stream<JValue> stream() {
            return value.stream();
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

        public Map<String, JValue> getValue() {
            return value;
        }

        public Optional<JValue> get(String name) {
            return Optional.ofNullable(value.get(name));
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

        public <B> List<B> mapToList(BiFunction<String, JValue, B> f) {
            return entrySet().stream().map(e -> f.apply(e.getKey(), e.getValue())).collect(Collectors.toList());
        }

        public <B> List<B> mapValues(Function<JValue, B> f) {
            return values().stream().map(f).collect(Collectors.toList());
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

    }
}
