package net.hamnaberg.json.codec;

import io.vavr.*;
import io.vavr.collection.List;
import io.vavr.control.Option;

import net.hamnaberg.json.Json;
import net.hamnaberg.json.util.*;

import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

public abstract class Codecs {
    private Codecs(){}

    public static final JsonCodec<Json.JValue> CIdentity = new DefaultJsonCodec<>(
            Decoders.DIdentity,
            Encoders.EIdentity,
            "Identity"
    );

    public static final JsonCodec<String> CString = new DefaultJsonCodec<>(
            Decoders.DString,
            Encoders.EString,
            "String"
    );

    public static final JsonCodec<Number> CNumber = new DefaultJsonCodec<>(
            Decoders.DNumber,
            Encoders.ENumber,
            "Number"
    );

    public static final JsonCodec<Long> CLong = new DefaultJsonCodec<>(
            Decoders.DLong,
            Encoders.ELong,
            "Long"
    );

    public static final JsonCodec<Double> CDouble = new DefaultJsonCodec<>(
            Decoders.DDouble,
            Encoders.EDouble,
            "Double"
    );

    public static final JsonCodec<Integer> CInt = new DefaultJsonCodec<>(
            Decoders.DInt,
            Encoders.EInt,
            "Int"
    );

    public static final JsonCodec<Boolean> CBoolean = new DefaultJsonCodec<>(
            Decoders.DBoolean,
            Encoders.EBoolean,
            "Boolean"
    );

    public static final JsonCodec<URI> CURI = new DefaultJsonCodec<>(
            Decoders.DURI,
            Encoders.EURI,
            "URI"
    );

    public static final JsonCodec<URL> CURL = new DefaultJsonCodec<>(
            Decoders.DURL,
            Encoders.EURL,
            "URL"
    );
    public static final JsonCodec<UUID> CUUID = new DefaultJsonCodec<>(
            Decoders.DUUID,
            Encoders.EUUID,
            "UUID"
    );

    public static final JsonCodec<ZonedDateTime> CISODateTimeUTC = new DefaultJsonCodec<>(
            Decoders.DISODateTimeUTC,
            Encoders.EISODateTimeUTC,
            "ZonedDateTime"
    );

    public static final JsonCodec<Instant> CISOInstantUTC = new DefaultJsonCodec<>(
            Decoders.DISOInstantUTC,
            Encoders.EISOInstantUTC,
            "Instant"
    );

    public static <A> JsonCodec<A> nullCodec() {
        return new DefaultJsonCodec<>(
                ignore -> DecodeResult.ok(null),
                ignore -> Json.jNull(),
                "Null"
        );
    }

    public static <A> JsonCodec<List<A>> listCodec(JsonCodec<A> codec) {
        DecodeJson<List<A>> decodeJson = value -> DecodeResult.sequence(value.asJsonArrayOrEmpty().mapToList(codec::fromJson));
        return new DefaultJsonCodec<>(
                decodeJson.withDefaultValue(List.empty()),
                value -> Json.jArray(value.map(codec::toJson)),
                String.format("ListCodec(%s)", codec.toString())
        );
    }

    public static <A> JsonCodec<java.util.List<A>> javaListCodec(JsonCodec<A> codec) {
        JsonCodec<java.util.List<A>> listCodec = listCodec(codec).xmap(List::toJavaList, List::ofAll);
        return JsonCodec.lift(listCodec.withDefaultValue(Collections.emptyList()), listCodec);
    }

    public static <A> JsonCodec<Option<A>> OptionCodec(JsonCodec<A> codec) {
        DecodeJson<Option<A>> decoder = value -> value.isNull() ? DecodeResult.ok(Option.none()) : DecodeResult.ok(codec.fromJson(value).toOption());
        EncodeJson<Option<A>> encoder = value -> value.map(codec::toJson).getOrElse(Json.jNull());
        return JsonCodec.lift(decoder.withDefaultValue(Option.none()), encoder);
    }

    public static <A> JsonCodec<Optional<A>> OptionalCodec(JsonCodec<A> underlying) {
        JsonCodec<Optional<A>> codec = OptionCodec(underlying).xmap(Option::toJavaOptional, Option::ofOptional);
        return JsonCodec.lift(codec.withDefaultValue(Optional.empty()), codec);
    }

    public static <A> JsonCodec<A> objectCodec(Function<Json.JObject, DecodeResult<A>> decoder, Function<A, Json.JObject> encoder) {
        return JsonCodec.lift(
                json -> decoder.apply(json.asJsonObjectOrEmpty()),
                a ->encoder.apply(a).asJValue()
        );
    }

    public static <A1> JsonCodec<A1> of(NamedJsonCodec<A1> c1) {
        return codec(c1);
    }

    public static <A1, A2> JsonCodec<Tuple2<A1, A2>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2) {
        return codec(Iso.identity(), c1, c2);
    }

    public static <A1, A2, A3> JsonCodec<Tuple3<A1, A2, A3>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3) {
        return codec(Iso.identity(), c1, c2, c3);
    }

    public static <A1, A2, A3, A4> JsonCodec<Tuple4<A1, A2, A3, A4>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4) {
        return codec(Iso.identity(), c1, c2, c3, c4);
    }

    public static <A1, A2, A3, A4, A5> JsonCodec<Tuple5<A1, A2, A3, A4, A5>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5);
    }

    public static <A1, A2, A3, A4, A5, A6> JsonCodec<Tuple6<A1, A2, A3, A4, A5, A6>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6);
    }

    public static <A1, A2, A3, A4, A5, A6, A7> JsonCodec<Tuple7<A1, A2, A3, A4, A5, A6, A7>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7);
    }

    public static <A1, A2, A3, A4, A5, A6, A7, A8> JsonCodec<Tuple8<A1, A2, A3, A4, A5, A6, A7, A8>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9> JsonCodec<Tuple9<A1, A2, A3, A4, A5, A6, A7, A8, A9>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10> JsonCodec<Tuple10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11> JsonCodec<Tuple11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12> JsonCodec<Tuple12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13> JsonCodec<Tuple13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14> JsonCodec<Tuple14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15> JsonCodec<Tuple15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16> JsonCodec<Tuple16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17> JsonCodec<Tuple17<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18> JsonCodec<Tuple18<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19> JsonCodec<Tuple19<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20> JsonCodec<Tuple20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21> JsonCodec<Tuple21<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22> JsonCodec<Tuple22<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23> JsonCodec<Tuple23<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24> JsonCodec<Tuple24<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23, NamedJsonCodec<A24> c24) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25> JsonCodec<Tuple25<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23, NamedJsonCodec<A24> c24, NamedJsonCodec<A25> c25) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26> JsonCodec<Tuple26<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23, NamedJsonCodec<A24> c24, NamedJsonCodec<A25> c25, NamedJsonCodec<A26> c26) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26);
    }


    public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27> JsonCodec<Tuple27<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27>> of(NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23, NamedJsonCodec<A24> c24, NamedJsonCodec<A25> c25, NamedJsonCodec<A26> c26, NamedJsonCodec<A27> c27) {
        return codec(Iso.identity(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27);
    }

    public static <TT> JsonCodec<TT> codec(NamedJsonCodec<TT> c1) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                return Json.jObject(
                        c1.name,
                        c1.toJson(value)
                ).asJValue();
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();

                return DecodeResult.decode(object, c1.name, c1);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());

                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2> JsonCodec<TT> codec(Iso<TT, Tuple2<A1, A2>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        Tuple2::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3> JsonCodec<TT> codec(Iso<TT, Tuple3<A1, A2, A3>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        Tuple3::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4> JsonCodec<TT> codec(Iso<TT, Tuple4<A1, A2, A3, A4>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        Tuple4::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5> JsonCodec<TT> codec(Iso<TT, Tuple5<A1, A2, A3, A4, A5>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        Tuple5::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6> JsonCodec<TT> codec(Iso<TT, Tuple6<A1, A2, A3, A4, A5, A6>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        Tuple6::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7> JsonCodec<TT> codec(Iso<TT, Tuple7<A1, A2, A3, A4, A5, A6, A7>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        Tuple7::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8> JsonCodec<TT> codec(Iso<TT, Tuple8<A1, A2, A3, A4, A5, A6, A7, A8>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        Tuple8::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9> JsonCodec<TT> codec(Iso<TT, Tuple9<A1, A2, A3, A4, A5, A6, A7, A8, A9>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        Tuple9::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10> JsonCodec<TT> codec(Iso<TT, Tuple10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        Tuple10::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11> JsonCodec<TT> codec(Iso<TT, Tuple11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        Tuple11::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12> JsonCodec<TT> codec(Iso<TT, Tuple12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        Tuple12::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13> JsonCodec<TT> codec(Iso<TT, Tuple13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        Tuple13::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14> JsonCodec<TT> codec(Iso<TT, Tuple14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        Tuple14::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15> JsonCodec<TT> codec(Iso<TT, Tuple15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        Tuple15::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16> JsonCodec<TT> codec(Iso<TT, Tuple16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder(),
                        c16.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        c16.toFieldDecoder(),
                        Tuple16::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                map.put(c16.name, c16.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17> JsonCodec<TT> codec(Iso<TT, Tuple17<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder(),
                        c16.toFieldEncoder(),
                        c17.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        c16.toFieldDecoder(),
                        c17.toFieldDecoder(),
                        Tuple17::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                map.put(c16.name, c16.codec.toString());
                map.put(c17.name, c17.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18> JsonCodec<TT> codec(Iso<TT, Tuple18<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder(),
                        c16.toFieldEncoder(),
                        c17.toFieldEncoder(),
                        c18.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        c16.toFieldDecoder(),
                        c17.toFieldDecoder(),
                        c18.toFieldDecoder(),
                        Tuple18::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                map.put(c16.name, c16.codec.toString());
                map.put(c17.name, c17.codec.toString());
                map.put(c18.name, c18.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19> JsonCodec<TT> codec(Iso<TT, Tuple19<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder(),
                        c16.toFieldEncoder(),
                        c17.toFieldEncoder(),
                        c18.toFieldEncoder(),
                        c19.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        c16.toFieldDecoder(),
                        c17.toFieldDecoder(),
                        c18.toFieldDecoder(),
                        c19.toFieldDecoder(),
                        Tuple19::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                map.put(c16.name, c16.codec.toString());
                map.put(c17.name, c17.codec.toString());
                map.put(c18.name, c18.codec.toString());
                map.put(c19.name, c19.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20> JsonCodec<TT> codec(Iso<TT, Tuple20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder(),
                        c16.toFieldEncoder(),
                        c17.toFieldEncoder(),
                        c18.toFieldEncoder(),
                        c19.toFieldEncoder(),
                        c20.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        c16.toFieldDecoder(),
                        c17.toFieldDecoder(),
                        c18.toFieldDecoder(),
                        c19.toFieldDecoder(),
                        c20.toFieldDecoder(),
                        Tuple20::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                map.put(c16.name, c16.codec.toString());
                map.put(c17.name, c17.codec.toString());
                map.put(c18.name, c18.codec.toString());
                map.put(c19.name, c19.codec.toString());
                map.put(c20.name, c20.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21> JsonCodec<TT> codec(Iso<TT, Tuple21<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder(),
                        c16.toFieldEncoder(),
                        c17.toFieldEncoder(),
                        c18.toFieldEncoder(),
                        c19.toFieldEncoder(),
                        c20.toFieldEncoder(),
                        c21.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        c16.toFieldDecoder(),
                        c17.toFieldDecoder(),
                        c18.toFieldDecoder(),
                        c19.toFieldDecoder(),
                        c20.toFieldDecoder(),
                        c21.toFieldDecoder(),
                        Tuple21::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                map.put(c16.name, c16.codec.toString());
                map.put(c17.name, c17.codec.toString());
                map.put(c18.name, c18.codec.toString());
                map.put(c19.name, c19.codec.toString());
                map.put(c20.name, c20.codec.toString());
                map.put(c21.name, c21.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22> JsonCodec<TT> codec(Iso<TT, Tuple22<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder(),
                        c16.toFieldEncoder(),
                        c17.toFieldEncoder(),
                        c18.toFieldEncoder(),
                        c19.toFieldEncoder(),
                        c20.toFieldEncoder(),
                        c21.toFieldEncoder(),
                        c22.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        c16.toFieldDecoder(),
                        c17.toFieldDecoder(),
                        c18.toFieldDecoder(),
                        c19.toFieldDecoder(),
                        c20.toFieldDecoder(),
                        c21.toFieldDecoder(),
                        c22.toFieldDecoder(),
                        Tuple22::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                map.put(c16.name, c16.codec.toString());
                map.put(c17.name, c17.codec.toString());
                map.put(c18.name, c18.codec.toString());
                map.put(c19.name, c19.codec.toString());
                map.put(c20.name, c20.codec.toString());
                map.put(c21.name, c21.codec.toString());
                map.put(c22.name, c22.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23> JsonCodec<TT> codec(Iso<TT, Tuple23<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder(),
                        c16.toFieldEncoder(),
                        c17.toFieldEncoder(),
                        c18.toFieldEncoder(),
                        c19.toFieldEncoder(),
                        c20.toFieldEncoder(),
                        c21.toFieldEncoder(),
                        c22.toFieldEncoder(),
                        c23.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        c16.toFieldDecoder(),
                        c17.toFieldDecoder(),
                        c18.toFieldDecoder(),
                        c19.toFieldDecoder(),
                        c20.toFieldDecoder(),
                        c21.toFieldDecoder(),
                        c22.toFieldDecoder(),
                        c23.toFieldDecoder(),
                        Tuple23::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                map.put(c16.name, c16.codec.toString());
                map.put(c17.name, c17.codec.toString());
                map.put(c18.name, c18.codec.toString());
                map.put(c19.name, c19.codec.toString());
                map.put(c20.name, c20.codec.toString());
                map.put(c21.name, c21.codec.toString());
                map.put(c22.name, c22.codec.toString());
                map.put(c23.name, c23.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24> JsonCodec<TT> codec(Iso<TT, Tuple24<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23, NamedJsonCodec<A24> c24) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder(),
                        c16.toFieldEncoder(),
                        c17.toFieldEncoder(),
                        c18.toFieldEncoder(),
                        c19.toFieldEncoder(),
                        c20.toFieldEncoder(),
                        c21.toFieldEncoder(),
                        c22.toFieldEncoder(),
                        c23.toFieldEncoder(),
                        c24.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        c16.toFieldDecoder(),
                        c17.toFieldDecoder(),
                        c18.toFieldDecoder(),
                        c19.toFieldDecoder(),
                        c20.toFieldDecoder(),
                        c21.toFieldDecoder(),
                        c22.toFieldDecoder(),
                        c23.toFieldDecoder(),
                        c24.toFieldDecoder(),
                        Tuple24::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                map.put(c16.name, c16.codec.toString());
                map.put(c17.name, c17.codec.toString());
                map.put(c18.name, c18.codec.toString());
                map.put(c19.name, c19.codec.toString());
                map.put(c20.name, c20.codec.toString());
                map.put(c21.name, c21.codec.toString());
                map.put(c22.name, c22.codec.toString());
                map.put(c23.name, c23.codec.toString());
                map.put(c24.name, c24.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25> JsonCodec<TT> codec(Iso<TT, Tuple25<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23, NamedJsonCodec<A24> c24, NamedJsonCodec<A25> c25) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder(),
                        c16.toFieldEncoder(),
                        c17.toFieldEncoder(),
                        c18.toFieldEncoder(),
                        c19.toFieldEncoder(),
                        c20.toFieldEncoder(),
                        c21.toFieldEncoder(),
                        c22.toFieldEncoder(),
                        c23.toFieldEncoder(),
                        c24.toFieldEncoder(),
                        c25.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        c16.toFieldDecoder(),
                        c17.toFieldDecoder(),
                        c18.toFieldDecoder(),
                        c19.toFieldDecoder(),
                        c20.toFieldDecoder(),
                        c21.toFieldDecoder(),
                        c22.toFieldDecoder(),
                        c23.toFieldDecoder(),
                        c24.toFieldDecoder(),
                        c25.toFieldDecoder(),
                        Tuple25::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                map.put(c16.name, c16.codec.toString());
                map.put(c17.name, c17.codec.toString());
                map.put(c18.name, c18.codec.toString());
                map.put(c19.name, c19.codec.toString());
                map.put(c20.name, c20.codec.toString());
                map.put(c21.name, c21.codec.toString());
                map.put(c22.name, c22.codec.toString());
                map.put(c23.name, c23.codec.toString());
                map.put(c24.name, c24.codec.toString());
                map.put(c25.name, c25.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26> JsonCodec<TT> codec(Iso<TT, Tuple26<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23, NamedJsonCodec<A24> c24, NamedJsonCodec<A25> c25, NamedJsonCodec<A26> c26) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder(),
                        c16.toFieldEncoder(),
                        c17.toFieldEncoder(),
                        c18.toFieldEncoder(),
                        c19.toFieldEncoder(),
                        c20.toFieldEncoder(),
                        c21.toFieldEncoder(),
                        c22.toFieldEncoder(),
                        c23.toFieldEncoder(),
                        c24.toFieldEncoder(),
                        c25.toFieldEncoder(),
                        c26.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        c16.toFieldDecoder(),
                        c17.toFieldDecoder(),
                        c18.toFieldDecoder(),
                        c19.toFieldDecoder(),
                        c20.toFieldDecoder(),
                        c21.toFieldDecoder(),
                        c22.toFieldDecoder(),
                        c23.toFieldDecoder(),
                        c24.toFieldDecoder(),
                        c25.toFieldDecoder(),
                        c26.toFieldDecoder(),
                        Tuple26::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                map.put(c16.name, c16.codec.toString());
                map.put(c17.name, c17.codec.toString());
                map.put(c18.name, c18.codec.toString());
                map.put(c19.name, c19.codec.toString());
                map.put(c20.name, c20.codec.toString());
                map.put(c21.name, c21.codec.toString());
                map.put(c22.name, c22.codec.toString());
                map.put(c23.name, c23.codec.toString());
                map.put(c24.name, c24.codec.toString());
                map.put(c25.name, c25.codec.toString());
                map.put(c26.name, c26.codec.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27> JsonCodec<TT> codec(Iso<TT, Tuple27<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23, NamedJsonCodec<A24> c24, NamedJsonCodec<A25> c25, NamedJsonCodec<A26> c26, NamedJsonCodec<A27> c27) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                return Encoders.encode(
                        c1.toFieldEncoder(),
                        c2.toFieldEncoder(),
                        c3.toFieldEncoder(),
                        c4.toFieldEncoder(),
                        c5.toFieldEncoder(),
                        c6.toFieldEncoder(),
                        c7.toFieldEncoder(),
                        c8.toFieldEncoder(),
                        c9.toFieldEncoder(),
                        c10.toFieldEncoder(),
                        c11.toFieldEncoder(),
                        c12.toFieldEncoder(),
                        c13.toFieldEncoder(),
                        c14.toFieldEncoder(),
                        c15.toFieldEncoder(),
                        c16.toFieldEncoder(),
                        c17.toFieldEncoder(),
                        c18.toFieldEncoder(),
                        c19.toFieldEncoder(),
                        c20.toFieldEncoder(),
                        c21.toFieldEncoder(),
                        c22.toFieldEncoder(),
                        c23.toFieldEncoder(),
                        c24.toFieldEncoder(),
                        c25.toFieldEncoder(),
                        c26.toFieldEncoder(),
                        c27.toFieldEncoder()
                ).toJson(iso.get(value));
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                return Decoders.decode(
                        c1.toFieldDecoder(),
                        c2.toFieldDecoder(),
                        c3.toFieldDecoder(),
                        c4.toFieldDecoder(),
                        c5.toFieldDecoder(),
                        c6.toFieldDecoder(),
                        c7.toFieldDecoder(),
                        c8.toFieldDecoder(),
                        c9.toFieldDecoder(),
                        c10.toFieldDecoder(),
                        c11.toFieldDecoder(),
                        c12.toFieldDecoder(),
                        c13.toFieldDecoder(),
                        c14.toFieldDecoder(),
                        c15.toFieldDecoder(),
                        c16.toFieldDecoder(),
                        c17.toFieldDecoder(),
                        c18.toFieldDecoder(),
                        c19.toFieldDecoder(),
                        c20.toFieldDecoder(),
                        c21.toFieldDecoder(),
                        c22.toFieldDecoder(),
                        c23.toFieldDecoder(),
                        c24.toFieldDecoder(),
                        c25.toFieldDecoder(),
                        c26.toFieldDecoder(),
                        c27.toFieldDecoder(),
                        Tuple27::new
                ).fromJson(value).map(iso::reverseGet);
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.codec.toString());
                map.put(c2.name, c2.codec.toString());
                map.put(c3.name, c3.codec.toString());
                map.put(c4.name, c4.codec.toString());
                map.put(c5.name, c5.codec.toString());
                map.put(c6.name, c6.codec.toString());
                map.put(c7.name, c7.codec.toString());
                map.put(c8.name, c8.codec.toString());
                map.put(c9.name, c9.codec.toString());
                map.put(c10.name, c10.codec.toString());
                map.put(c11.name, c11.codec.toString());
                map.put(c12.name, c12.codec.toString());
                map.put(c13.name, c13.codec.toString());
                map.put(c14.name, c14.codec.toString());
                map.put(c15.name, c15.codec.toString());
                map.put(c16.name, c16.codec.toString());
                map.put(c17.name, c17.codec.toString());
                map.put(c18.name, c18.codec.toString());
                map.put(c19.name, c19.codec.toString());
                map.put(c20.name, c20.codec.toString());
                map.put(c21.name, c21.codec.toString());
                map.put(c22.name, c22.codec.toString());
                map.put(c23.name, c23.codec.toString());
                map.put(c24.name, c24.codec.toString());
                map.put(c25.name, c25.codec.toString());
                map.put(c26.name, c26.codec.toString());
                map.put(c27.name, c27.codec.toString());
                return "codec" + map.toString();
            }
        };
    }
}
