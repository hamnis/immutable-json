package net.hamnaberg.json.extract;

import io.vavr.collection.List;
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

    public static <A> Option<A> mapObject(Json.JValue v, Function<ObjectMapper, A> f) {
        return v.asJsonObject().flatMap(ob -> Option.of(f.apply(new ObjectMapper(ob))));
    }

    public static <A> List<A> mapObjectArray(Json.JValue v, Function<ObjectMapper, A> f) {
        return v.asJsonArrayOrEmpty().getListAsObjects().map(ob -> f.apply(new ObjectMapper(ob)));
    }

    public static <A> List<A> mapNamedObjectArray(String name, Json.JValue v, Function<ObjectMapper, A> f) {
        return v.asJsonObject().map(o -> o.getAsArrayOrEmpty(name).getListAsObjects().map(e -> f.apply(new ObjectMapper(e)))).getOrElse(List.empty());
    }

    public static <A> Option<A> mapObject(Json.JObject v, Function<ObjectMapper, A> f) {
        return Option.of(f.apply(new ObjectMapper(v)));
    }

    public Integer integer(String name) {
        return o.getAsInteger(name).getOrElseThrow(() -> new NoSuchElementException("Missing " + name ));
    }

    public Long lng(String name) {
        return o.getAsLong(name).getOrElseThrow(() -> new NoSuchElementException("Missing " + name ));
    }

    public Option<Long> lngOpt(String name) {
        return o.getAsLong(name);
    }

    public Option<Integer> integerOpt(String name) {
        return o.getAsInteger(name);
    }

    public String string(String name) {
        return o.getAsString(name).getOrElseThrow(() -> new NoSuchElementException("Missing " + name ));
    }

    public Option<String> stringOpt(String name) {
        return o.getAsString(name);
    }

    public Double dble(String name) {
        return o.getAsDouble(name).getOrElseThrow(() -> new NoSuchElementException("Missing " + name ));
    }

    public Option<Double> dbleOpt(String name) {
        return o.getAsDouble(name);
    }

    public Boolean bool(String name) {
        return o.getAsBoolean(name).getOrElseThrow(() -> new NoSuchElementException("Missing " + name ));
    }

    public Option<Boolean> boolOpt(String name) {
        return o.getAsBoolean(name);
    }

    public <A> List<A> list(String name, Function<ObjectMapper, A> f) {
        return o.getAsArrayOrEmpty(name).getListAsObjects().map(e -> f.apply(new ObjectMapper(e)));
    }

    public <A> A[] array(String name, Function<ObjectMapper, A> f, Class<A> type) {
        return o.getAsArrayOrEmpty(name).getListAsObjects().map(e -> f.apply(new ObjectMapper(e))).toJavaArray(type);
    }

    public List<String> stringList(String name) {
        return o.getAsArrayOrEmpty(name).getListAsStrings();
    }

    public <A> A decode(String name, DecodeJson<A> decoder) {
        Json.JValue value = o.get(name).getOrElseThrow(() -> new NoSuchElementException("Missing " + name ));
        return decoder.fromJsonUnsafe(value);
    }

    public <A> A object(String name, Function<ObjectMapper, A> f) {
        return o.getAsObject(name).map(obj -> f.apply(new ObjectMapper(obj))).getOrElseThrow(() -> new NoSuchElementException("Missing " + name ));
    }

    public <A> Option<A> objectOpt(String name, Function<ObjectMapper, A> f) {
        return o.getAsObject(name).flatMap(obj -> obj.keySet().isEmpty() ?  Option.none() : Option.of(f.apply(new ObjectMapper(obj))));
    }

    public ObjectMapper downField(String name) {
        return o.getAsObject(name).map(ObjectMapper::new).getOrElseThrow(() -> new NoSuchElementException("Missing " + name));
    }
}
