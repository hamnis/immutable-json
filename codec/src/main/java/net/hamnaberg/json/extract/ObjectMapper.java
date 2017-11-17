package net.hamnaberg.json.extract;

import io.vavr.collection.List;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.codec.DecodeJson;

import java.util.NoSuchElementException;
import java.util.function.Function;

public final class ObjectMapper {
    private final Json.JObject o;

    private ObjectMapper(Json.JObject o) {
        this.o = o;
    }

    public static ObjectMapper wrap(Json.JObject value) {
        return new ObjectMapper(value);
    }

    public static <A> Option<A> mapObject(Json.JValue object, Function<ObjectMapper, A> f) {
        return object.asJsonObject().flatMap(ob -> Option.of(f.apply(wrap(ob))));
    }

    public static <A> List<A> mapObjectArray(Json.JValue array, Function<ObjectMapper, A> f) {
        return array.asJsonArrayOrEmpty().getListAsObjects().map(ob -> f.apply(wrap(ob)));
    }

    public static <A> List<A> mapNamedObjectArray(Json.JValue object, String name, Function<ObjectMapper, A> f) {
        return object.asJsonObject().map(o -> o.getAsArrayOrEmpty(name).getListAsObjects().map(e -> f.apply(wrap(e)))).getOrElse(List.empty());
    }

    public static <A> Option<A> mapObject(Json.JObject object, Function<ObjectMapper, A> f) {
        return Option.of(f.apply(wrap(object)));
    }

    public Integer integer(String name) {
        return getOrElseThrow(name, o.getAsInteger(name));
    }

    public Long lng(String name) {
        return getOrElseThrow(name, o.getAsLong(name));
    }

    public Option<Long> lngOpt(String name) {
        return o.getAsLong(name);
    }

    public Option<Integer> integerOpt(String name) {
        return o.getAsInteger(name);
    }

    public String string(String name) {
        return getOrElseThrow(name, o.getAsString(name));
    }

    public Option<String> stringOpt(String name) {
        return o.getAsString(name);
    }

    public Double dble(String name) {
        return getOrElseThrow(name, o.getAsDouble(name));
    }

    public Option<Double> dbleOpt(String name) {
        return o.getAsDouble(name);
    }

    public Boolean bool(String name) {
        return getOrElseThrow(name, o.getAsBoolean(name));
    }

    public Option<Boolean> boolOpt(String name) {
        return o.getAsBoolean(name);
    }

    public <A> List<A> list(String name, Function<ObjectMapper, A> f) {
        return o.getAsArrayOrEmpty(name).getListAsObjects().map(e -> f.apply(wrap(e)));
    }

    public <A> Vector<A> vector(String name, Function<ObjectMapper, A> f) {
        return list(name, f).toVector();
    }

    public <A> A[] array(String name, Function<ObjectMapper, A> f, Class<A> type) {
        return o.getAsArrayOrEmpty(name).getListAsObjects().map(e -> f.apply(wrap(e))).toJavaArray(type);
    }

    public List<String> stringList(String name) {
        return o.getAsArrayOrEmpty(name).getListAsStrings();
    }

    public <A> A decode(String name, DecodeJson<A> decoder) {
        return decoder.fromJsonUnsafe(getOrElseThrow(name, o.get(name)));
    }

    public <A> Option<A> objectOpt(String name, Function<ObjectMapper, A> f) {
        return o.getAsObject(name).filter(j -> !j.isEmpty()).flatMap(obj -> Option.of(f.apply(wrap(obj))));
    }

    public <A> A object(String name, Function<ObjectMapper, A> f) {
        return getOrElseThrow(name, objectOpt(name, f));
    }

    public ObjectMapper downField(String name) {
        return getOrElseThrow(name, o.getAsObject(name).map(ObjectMapper::new));
    }

    private <A> A getOrElseThrow(String name, Option<A> value) {
        return value.getOrElseThrow(() -> new NoSuchElementException("Missing " + name ));
    }
}
