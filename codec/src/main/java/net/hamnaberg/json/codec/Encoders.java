package net.hamnaberg.json.codec;

import io.vavr.*;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.util.*;

import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public abstract class Encoders {
    private Encoders(){}

    public static final EncodeJson<Json.JValue> EIdentity = j -> j;
    public static final EncodeJson<String> EString = Json::jString;
    public static final EncodeJson<Number> ENumber = Json::jNumber;
    public static final EncodeJson<Long> ELong = Json::jNumber;
    public static final EncodeJson<Integer> EInt = Json::jNumber;
    public static final EncodeJson<Double> EDouble = Json::jNumber;
    public static final EncodeJson<Boolean> EBoolean = Json::jBoolean;
    public static final EncodeJson<URI> EURI = EString.contramap(URI::toString);
    public static final EncodeJson<URL> EURL = EString.contramap(URL::toExternalForm);
    public static final EncodeJson<UUID> EUUID = EString.contramap(UUID::toString);
    public static final EncodeJson<ZonedDateTime> EISODateTimeUTC = zonedDateTimeEncoder(DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC));
    public static final EncodeJson<Instant> EISOInstantUTC = instantEncoder(DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC));

    public static EncodeJson<ZonedDateTime> zonedDateTimeEncoder(DateTimeFormatter formatter) {
        return EString.contramap(formatter::format);
    }

    public static EncodeJson<Instant> instantEncoder(DateTimeFormatter formatter) {
        return EString.contramap(formatter::format);
    }

    public static <A> EncodeJson<List<A>> listEncoder(EncodeJson<A> encoder) {
        return value -> Json.jArray(value.map(encoder::toJson));
    }

    public static <A> EncodeJson<java.util.List<A>> javaListEncoder(EncodeJson<A> encoder) {
        return listEncoder(encoder).contramap(List::ofAll);
    }

    public static <A> EncodeJson<Option<A>> OptionEncoder(EncodeJson<A> encoder) {
        return value -> value.map(encoder::toJson).getOrElse(Json.jNull());
    }

    public static <A> EncodeJson<Optional<A>> OptionalEncoder(EncodeJson<A> underlying) {
        return OptionEncoder(underlying).contramap(Option::ofOptional);
    }

    public static <A> EncodeJson<A> objectEncoder(Function<A, Json.JObject> encoder) {
        return a -> encoder.apply(a).asJValue();
    }
    public static <A1, A2> EncodeJson<Tuple2<A1, A2>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2))
        );
    }

    public static <A1, A2, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, Function<TT, Tuple2<A1, A2>> f) {
        return type -> encode(e1, e2).toJson(f.apply(type));
    }

    public static <A1, A2, A3> EncodeJson<Tuple3<A1, A2, A3>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3))
        );
    }

    public static <A1, A2, A3, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, Function<TT, Tuple3<A1, A2, A3>> f) {
        return type -> encode(e1, e2, e3).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4> EncodeJson<Tuple4<A1, A2, A3, A4>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4))
        );
    }

    public static <A1, A2, A3, A4, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, Function<TT, Tuple4<A1, A2, A3, A4>> f) {
        return type -> encode(e1, e2, e3, e4).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5> EncodeJson<Tuple5<A1, A2, A3, A4, A5>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5))
        );
    }

    public static <A1, A2, A3, A4, A5, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, Function<TT, Tuple5<A1, A2, A3, A4, A5>> f) {
        return type -> encode(e1, e2, e3, e4, e5).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6> EncodeJson<Tuple6<A1, A2, A3, A4, A5, A6>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, Function<TT, Tuple6<A1, A2, A3, A4, A5, A6>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7> EncodeJson<Tuple7<A1, A2, A3, A4, A5, A6, A7>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, Function<TT, Tuple7<A1, A2, A3, A4, A5, A6, A7>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8> EncodeJson<Tuple8<A1, A2, A3, A4, A5, A6, A7, A8>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, Function<TT, Tuple8<A1, A2, A3, A4, A5, A6, A7, A8>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9> EncodeJson<Tuple9<A1, A2, A3, A4, A5, A6, A7, A8, A9>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, Function<TT, Tuple9<A1, A2, A3, A4, A5, A6, A7, A8, A9>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10> EncodeJson<Tuple10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, Function<TT, Tuple10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11> EncodeJson<Tuple11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, Function<TT, Tuple11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12> EncodeJson<Tuple12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, Function<TT, Tuple12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13> EncodeJson<Tuple13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, Function<TT, Tuple13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14> EncodeJson<Tuple14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, Function<TT, Tuple14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15> EncodeJson<Tuple15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, Function<TT, Tuple15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16> EncodeJson<Tuple16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15)),
                Json.tuple(e16.name, e16.toJson(tuple._16))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, Function<TT, Tuple16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17> EncodeJson<Tuple17<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15)),
                Json.tuple(e16.name, e16.toJson(tuple._16)),
                Json.tuple(e17.name, e17.toJson(tuple._17))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, Function<TT, Tuple17<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18> EncodeJson<Tuple18<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15)),
                Json.tuple(e16.name, e16.toJson(tuple._16)),
                Json.tuple(e17.name, e17.toJson(tuple._17)),
                Json.tuple(e18.name, e18.toJson(tuple._18))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, Function<TT, Tuple18<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19> EncodeJson<Tuple19<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15)),
                Json.tuple(e16.name, e16.toJson(tuple._16)),
                Json.tuple(e17.name, e17.toJson(tuple._17)),
                Json.tuple(e18.name, e18.toJson(tuple._18)),
                Json.tuple(e19.name, e19.toJson(tuple._19))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, Function<TT, Tuple19<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20> EncodeJson<Tuple20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15)),
                Json.tuple(e16.name, e16.toJson(tuple._16)),
                Json.tuple(e17.name, e17.toJson(tuple._17)),
                Json.tuple(e18.name, e18.toJson(tuple._18)),
                Json.tuple(e19.name, e19.toJson(tuple._19)),
                Json.tuple(e20.name, e20.toJson(tuple._20))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, Function<TT, Tuple20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21> EncodeJson<Tuple21<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15)),
                Json.tuple(e16.name, e16.toJson(tuple._16)),
                Json.tuple(e17.name, e17.toJson(tuple._17)),
                Json.tuple(e18.name, e18.toJson(tuple._18)),
                Json.tuple(e19.name, e19.toJson(tuple._19)),
                Json.tuple(e20.name, e20.toJson(tuple._20)),
                Json.tuple(e21.name, e21.toJson(tuple._21))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, Function<TT, Tuple21<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22> EncodeJson<Tuple22<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, FieldEncoder<A22> e22) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15)),
                Json.tuple(e16.name, e16.toJson(tuple._16)),
                Json.tuple(e17.name, e17.toJson(tuple._17)),
                Json.tuple(e18.name, e18.toJson(tuple._18)),
                Json.tuple(e19.name, e19.toJson(tuple._19)),
                Json.tuple(e20.name, e20.toJson(tuple._20)),
                Json.tuple(e21.name, e21.toJson(tuple._21)),
                Json.tuple(e22.name, e22.toJson(tuple._22))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, FieldEncoder<A22> e22, Function<TT, Tuple22<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21, e22).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23> EncodeJson<Tuple23<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, FieldEncoder<A22> e22, FieldEncoder<A23> e23) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15)),
                Json.tuple(e16.name, e16.toJson(tuple._16)),
                Json.tuple(e17.name, e17.toJson(tuple._17)),
                Json.tuple(e18.name, e18.toJson(tuple._18)),
                Json.tuple(e19.name, e19.toJson(tuple._19)),
                Json.tuple(e20.name, e20.toJson(tuple._20)),
                Json.tuple(e21.name, e21.toJson(tuple._21)),
                Json.tuple(e22.name, e22.toJson(tuple._22)),
                Json.tuple(e23.name, e23.toJson(tuple._23))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, FieldEncoder<A22> e22, FieldEncoder<A23> e23, Function<TT, Tuple23<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21, e22, e23).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24> EncodeJson<Tuple24<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, FieldEncoder<A22> e22, FieldEncoder<A23> e23, FieldEncoder<A24> e24) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15)),
                Json.tuple(e16.name, e16.toJson(tuple._16)),
                Json.tuple(e17.name, e17.toJson(tuple._17)),
                Json.tuple(e18.name, e18.toJson(tuple._18)),
                Json.tuple(e19.name, e19.toJson(tuple._19)),
                Json.tuple(e20.name, e20.toJson(tuple._20)),
                Json.tuple(e21.name, e21.toJson(tuple._21)),
                Json.tuple(e22.name, e22.toJson(tuple._22)),
                Json.tuple(e23.name, e23.toJson(tuple._23)),
                Json.tuple(e24.name, e24.toJson(tuple._24))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, FieldEncoder<A22> e22, FieldEncoder<A23> e23, FieldEncoder<A24> e24, Function<TT, Tuple24<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21, e22, e23, e24).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25> EncodeJson<Tuple25<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, FieldEncoder<A22> e22, FieldEncoder<A23> e23, FieldEncoder<A24> e24, FieldEncoder<A25> e25) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15)),
                Json.tuple(e16.name, e16.toJson(tuple._16)),
                Json.tuple(e17.name, e17.toJson(tuple._17)),
                Json.tuple(e18.name, e18.toJson(tuple._18)),
                Json.tuple(e19.name, e19.toJson(tuple._19)),
                Json.tuple(e20.name, e20.toJson(tuple._20)),
                Json.tuple(e21.name, e21.toJson(tuple._21)),
                Json.tuple(e22.name, e22.toJson(tuple._22)),
                Json.tuple(e23.name, e23.toJson(tuple._23)),
                Json.tuple(e24.name, e24.toJson(tuple._24)),
                Json.tuple(e25.name, e25.toJson(tuple._25))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, FieldEncoder<A22> e22, FieldEncoder<A23> e23, FieldEncoder<A24> e24, FieldEncoder<A25> e25, Function<TT, Tuple25<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21, e22, e23, e24, e25).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26> EncodeJson<Tuple26<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, FieldEncoder<A22> e22, FieldEncoder<A23> e23, FieldEncoder<A24> e24, FieldEncoder<A25> e25, FieldEncoder<A26> e26) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15)),
                Json.tuple(e16.name, e16.toJson(tuple._16)),
                Json.tuple(e17.name, e17.toJson(tuple._17)),
                Json.tuple(e18.name, e18.toJson(tuple._18)),
                Json.tuple(e19.name, e19.toJson(tuple._19)),
                Json.tuple(e20.name, e20.toJson(tuple._20)),
                Json.tuple(e21.name, e21.toJson(tuple._21)),
                Json.tuple(e22.name, e22.toJson(tuple._22)),
                Json.tuple(e23.name, e23.toJson(tuple._23)),
                Json.tuple(e24.name, e24.toJson(tuple._24)),
                Json.tuple(e25.name, e25.toJson(tuple._25)),
                Json.tuple(e26.name, e26.toJson(tuple._26))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, FieldEncoder<A22> e22, FieldEncoder<A23> e23, FieldEncoder<A24> e24, FieldEncoder<A25> e25, FieldEncoder<A26> e26, Function<TT, Tuple26<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21, e22, e23, e24, e25, e26).toJson(f.apply(type));
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27> EncodeJson<Tuple27<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27>> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, FieldEncoder<A22> e22, FieldEncoder<A23> e23, FieldEncoder<A24> e24, FieldEncoder<A25> e25, FieldEncoder<A26> e26, FieldEncoder<A27> e27) {
        return tuple -> Json.jObject(
                Json.tuple(e1.name, e1.toJson(tuple._1)),
                Json.tuple(e2.name, e2.toJson(tuple._2)),
                Json.tuple(e3.name, e3.toJson(tuple._3)),
                Json.tuple(e4.name, e4.toJson(tuple._4)),
                Json.tuple(e5.name, e5.toJson(tuple._5)),
                Json.tuple(e6.name, e6.toJson(tuple._6)),
                Json.tuple(e7.name, e7.toJson(tuple._7)),
                Json.tuple(e8.name, e8.toJson(tuple._8)),
                Json.tuple(e9.name, e9.toJson(tuple._9)),
                Json.tuple(e10.name, e10.toJson(tuple._10)),
                Json.tuple(e11.name, e11.toJson(tuple._11)),
                Json.tuple(e12.name, e12.toJson(tuple._12)),
                Json.tuple(e13.name, e13.toJson(tuple._13)),
                Json.tuple(e14.name, e14.toJson(tuple._14)),
                Json.tuple(e15.name, e15.toJson(tuple._15)),
                Json.tuple(e16.name, e16.toJson(tuple._16)),
                Json.tuple(e17.name, e17.toJson(tuple._17)),
                Json.tuple(e18.name, e18.toJson(tuple._18)),
                Json.tuple(e19.name, e19.toJson(tuple._19)),
                Json.tuple(e20.name, e20.toJson(tuple._20)),
                Json.tuple(e21.name, e21.toJson(tuple._21)),
                Json.tuple(e22.name, e22.toJson(tuple._22)),
                Json.tuple(e23.name, e23.toJson(tuple._23)),
                Json.tuple(e24.name, e24.toJson(tuple._24)),
                Json.tuple(e25.name, e25.toJson(tuple._25)),
                Json.tuple(e26.name, e26.toJson(tuple._26)),
                Json.tuple(e27.name, e27.toJson(tuple._27))
        );
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27, TT> EncodeJson<TT> encode(FieldEncoder<A1> e1, FieldEncoder<A2> e2, FieldEncoder<A3> e3, FieldEncoder<A4> e4, FieldEncoder<A5> e5, FieldEncoder<A6> e6, FieldEncoder<A7> e7, FieldEncoder<A8> e8, FieldEncoder<A9> e9, FieldEncoder<A10> e10, FieldEncoder<A11> e11, FieldEncoder<A12> e12, FieldEncoder<A13> e13, FieldEncoder<A14> e14, FieldEncoder<A15> e15, FieldEncoder<A16> e16, FieldEncoder<A17> e17, FieldEncoder<A18> e18, FieldEncoder<A19> e19, FieldEncoder<A20> e20, FieldEncoder<A21> e21, FieldEncoder<A22> e22, FieldEncoder<A23> e23, FieldEncoder<A24> e24, FieldEncoder<A25> e25, FieldEncoder<A26> e26, FieldEncoder<A27> e27, Function<TT, Tuple27<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27>> f) {
        return type -> encode(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21, e22, e23, e24, e25, e26, e27).toJson(f.apply(type));
    }
}
