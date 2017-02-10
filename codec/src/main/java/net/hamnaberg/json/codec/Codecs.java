package net.hamnaberg.json.codec;

import javaslang.*;
import javaslang.collection.List;
import javaslang.control.Option;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.util.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class Codecs {
    private Codecs(){}

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
                map.put(c1.name, c1.toString());

                return "codec" + map.toString();
            }
        };
    }

    public static <TT, A, B> JsonCodec<TT> codec(Iso<TT, Tuple2<A, B>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple2<A, B> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();

                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                return oa.flatMap(a -> ob.flatMap(b -> DecodeResult.ok(iso.reverseGet(new Tuple2<>(a,b)))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());

                return "codec" + map.toString();
            }
        };
    }



    public static <TT, A, B, C> JsonCodec<TT> codec(Iso<TT, Tuple3<A, B, C>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple3<A, B, C> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<C> oc = DecodeResult.decode(object, c3.name, c3);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> DecodeResult.ok(iso.reverseGet(new Tuple3<>(a,b,c))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());

                return "codec" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D> JsonCodec<TT> codec(Iso<TT, Tuple4<A, B, C, D>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple4<A, B, C, D> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<C> oc = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<D> od = DecodeResult.decode(object, c4.name, c4);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> DecodeResult.ok(iso.reverseGet(new Tuple4<>(a,b,c,d)))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());

                return "codec" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D, E> JsonCodec<TT> codec(Iso<TT, Tuple5<A, B, C, D, E>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4, NamedJsonCodec<E> c5) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple5<A, B, C, D, E> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<C> oc = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<D> od = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<E> oe = DecodeResult.decode(object, c5.name, c5);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> DecodeResult.ok(iso.reverseGet(new Tuple5<>(a,b,c,d,e))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());

                return "codec" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D, E, F> JsonCodec<TT> codec(Iso<TT, Tuple6<A, B, C, D, E, F>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4, NamedJsonCodec<E> c5, NamedJsonCodec<F> c6) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple6<A, B, C, D, E, F> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<C> oc = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<D> od = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<E> oe = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<F> of = DecodeResult.decode(object, c6.name, c6);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> DecodeResult.ok(iso.reverseGet(new Tuple6<>(a,b,c,d,e,f)))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());

                return "codec" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D, E, F, G> JsonCodec<TT> codec(Iso<TT, Tuple7<A, B, C, D, E, F, G>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4, NamedJsonCodec<E> c5, NamedJsonCodec<F> c6, NamedJsonCodec<G> c7) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple7<A, B, C, D, E, F, G> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<C> oc = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<D> od = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<E> oe = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<F> of = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<G> og = DecodeResult.decode(object, c7.name, c7);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> DecodeResult.ok(iso.reverseGet(new Tuple7<>(a,b,c,d,e,f,g))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());

                return "codec" + map.toString();
            }
        };
    }



    public static <TT, A, B, C, D, E, F, G, H> JsonCodec<TT> codec(Iso<TT, Tuple8<A, B, C, D, E, F, G, H>> iso, NamedJsonCodec<A> c1, NamedJsonCodec<B> c2, NamedJsonCodec<C> c3, NamedJsonCodec<D> c4, NamedJsonCodec<E> c5, NamedJsonCodec<F> c6, NamedJsonCodec<G> c7, NamedJsonCodec<H> c8) {
        return new JsonCodec<TT>() {
            @Override
            public Json.JValue toJson(TT value) {
                Tuple8<A, B, C, D, E, F, G, H> tuple = iso.get(value);

                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A> oa = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<B> ob = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<C> oc = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<D> od = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<E> oe = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<F> of = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<G> og = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<H> oh = DecodeResult.decode(object, c8.name, c8);
                return oa.flatMap(a -> ob.flatMap(b -> oc.flatMap(c -> od.flatMap(d -> oe.flatMap(e -> of.flatMap(f -> og.flatMap(g -> oh.flatMap(h -> DecodeResult.ok(iso.reverseGet(new Tuple8<>(a,b,c,d,e,f,g,h)))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());

                return "codec" + map.toString();
            }
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9> JsonCodec<TT> codec(Iso<TT, Tuple9<A1, A2, A3, A4, A5, A6, A7, A8, A9>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple9<A1, A2, A3, A4, A5, A6, A7, A8, A9> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 ->  DecodeResult.ok(iso.reverseGet(new Tuple9<>(v1, v2, v3, v4, v5, v6, v7, v8, v9))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10> JsonCodec<TT> codec(Iso<TT, Tuple10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 ->  DecodeResult.ok(iso.reverseGet(new Tuple10<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11> JsonCodec<TT> codec(Iso<TT, Tuple11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 ->  DecodeResult.ok(iso.reverseGet(new Tuple11<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12> JsonCodec<TT> codec(Iso<TT, Tuple12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 ->  DecodeResult.ok(iso.reverseGet(new Tuple12<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13> JsonCodec<TT> codec(Iso<TT, Tuple13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 ->  DecodeResult.ok(iso.reverseGet(new Tuple13<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14> JsonCodec<TT> codec(Iso<TT, Tuple14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 ->  DecodeResult.ok(iso.reverseGet(new Tuple14<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15> JsonCodec<TT> codec(Iso<TT, Tuple15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 ->  DecodeResult.ok(iso.reverseGet(new Tuple15<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16> JsonCodec<TT> codec(Iso<TT, Tuple16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15)),
                        Json.tuple(c16.name, c16.toJson(tuple._16))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                DecodeResult<A16> d16 = DecodeResult.decode(object, c16.name, c16);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 ->  DecodeResult.ok(iso.reverseGet(new Tuple16<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                map.put(c16.name, c16.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17> JsonCodec<TT> codec(Iso<TT, Tuple17<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple17<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15)),
                        Json.tuple(c16.name, c16.toJson(tuple._16)),
                        Json.tuple(c17.name, c17.toJson(tuple._17))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                DecodeResult<A16> d16 = DecodeResult.decode(object, c16.name, c16);
                DecodeResult<A17> d17 = DecodeResult.decode(object, c17.name, c17);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 ->  DecodeResult.ok(iso.reverseGet(new Tuple17<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17))))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                map.put(c16.name, c16.toString());
                map.put(c17.name, c17.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18> JsonCodec<TT> codec(Iso<TT, Tuple18<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple18<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15)),
                        Json.tuple(c16.name, c16.toJson(tuple._16)),
                        Json.tuple(c17.name, c17.toJson(tuple._17)),
                        Json.tuple(c18.name, c18.toJson(tuple._18))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                DecodeResult<A16> d16 = DecodeResult.decode(object, c16.name, c16);
                DecodeResult<A17> d17 = DecodeResult.decode(object, c17.name, c17);
                DecodeResult<A18> d18 = DecodeResult.decode(object, c18.name, c18);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 ->  DecodeResult.ok(iso.reverseGet(new Tuple18<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)))))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                map.put(c16.name, c16.toString());
                map.put(c17.name, c17.toString());
                map.put(c18.name, c18.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19> JsonCodec<TT> codec(Iso<TT, Tuple19<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple19<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15)),
                        Json.tuple(c16.name, c16.toJson(tuple._16)),
                        Json.tuple(c17.name, c17.toJson(tuple._17)),
                        Json.tuple(c18.name, c18.toJson(tuple._18)),
                        Json.tuple(c19.name, c19.toJson(tuple._19))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                DecodeResult<A16> d16 = DecodeResult.decode(object, c16.name, c16);
                DecodeResult<A17> d17 = DecodeResult.decode(object, c17.name, c17);
                DecodeResult<A18> d18 = DecodeResult.decode(object, c18.name, c18);
                DecodeResult<A19> d19 = DecodeResult.decode(object, c19.name, c19);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 ->  DecodeResult.ok(iso.reverseGet(new Tuple19<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19))))))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                map.put(c16.name, c16.toString());
                map.put(c17.name, c17.toString());
                map.put(c18.name, c18.toString());
                map.put(c19.name, c19.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20> JsonCodec<TT> codec(Iso<TT, Tuple20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15)),
                        Json.tuple(c16.name, c16.toJson(tuple._16)),
                        Json.tuple(c17.name, c17.toJson(tuple._17)),
                        Json.tuple(c18.name, c18.toJson(tuple._18)),
                        Json.tuple(c19.name, c19.toJson(tuple._19)),
                        Json.tuple(c20.name, c20.toJson(tuple._20))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                DecodeResult<A16> d16 = DecodeResult.decode(object, c16.name, c16);
                DecodeResult<A17> d17 = DecodeResult.decode(object, c17.name, c17);
                DecodeResult<A18> d18 = DecodeResult.decode(object, c18.name, c18);
                DecodeResult<A19> d19 = DecodeResult.decode(object, c19.name, c19);
                DecodeResult<A20> d20 = DecodeResult.decode(object, c20.name, c20);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 ->  DecodeResult.ok(iso.reverseGet(new Tuple20<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)))))))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                map.put(c16.name, c16.toString());
                map.put(c17.name, c17.toString());
                map.put(c18.name, c18.toString());
                map.put(c19.name, c19.toString());
                map.put(c20.name, c20.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21> JsonCodec<TT> codec(Iso<TT, Tuple21<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple21<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15)),
                        Json.tuple(c16.name, c16.toJson(tuple._16)),
                        Json.tuple(c17.name, c17.toJson(tuple._17)),
                        Json.tuple(c18.name, c18.toJson(tuple._18)),
                        Json.tuple(c19.name, c19.toJson(tuple._19)),
                        Json.tuple(c20.name, c20.toJson(tuple._20)),
                        Json.tuple(c21.name, c21.toJson(tuple._21))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                DecodeResult<A16> d16 = DecodeResult.decode(object, c16.name, c16);
                DecodeResult<A17> d17 = DecodeResult.decode(object, c17.name, c17);
                DecodeResult<A18> d18 = DecodeResult.decode(object, c18.name, c18);
                DecodeResult<A19> d19 = DecodeResult.decode(object, c19.name, c19);
                DecodeResult<A20> d20 = DecodeResult.decode(object, c20.name, c20);
                DecodeResult<A21> d21 = DecodeResult.decode(object, c21.name, c21);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 ->  DecodeResult.ok(iso.reverseGet(new Tuple21<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21))))))))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                map.put(c16.name, c16.toString());
                map.put(c17.name, c17.toString());
                map.put(c18.name, c18.toString());
                map.put(c19.name, c19.toString());
                map.put(c20.name, c20.toString());
                map.put(c21.name, c21.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22> JsonCodec<TT> codec(Iso<TT, Tuple22<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple22<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15)),
                        Json.tuple(c16.name, c16.toJson(tuple._16)),
                        Json.tuple(c17.name, c17.toJson(tuple._17)),
                        Json.tuple(c18.name, c18.toJson(tuple._18)),
                        Json.tuple(c19.name, c19.toJson(tuple._19)),
                        Json.tuple(c20.name, c20.toJson(tuple._20)),
                        Json.tuple(c21.name, c21.toJson(tuple._21)),
                        Json.tuple(c22.name, c22.toJson(tuple._22))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                DecodeResult<A16> d16 = DecodeResult.decode(object, c16.name, c16);
                DecodeResult<A17> d17 = DecodeResult.decode(object, c17.name, c17);
                DecodeResult<A18> d18 = DecodeResult.decode(object, c18.name, c18);
                DecodeResult<A19> d19 = DecodeResult.decode(object, c19.name, c19);
                DecodeResult<A20> d20 = DecodeResult.decode(object, c20.name, c20);
                DecodeResult<A21> d21 = DecodeResult.decode(object, c21.name, c21);
                DecodeResult<A22> d22 = DecodeResult.decode(object, c22.name, c22);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 ->  DecodeResult.ok(iso.reverseGet(new Tuple22<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22)))))))))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                map.put(c16.name, c16.toString());
                map.put(c17.name, c17.toString());
                map.put(c18.name, c18.toString());
                map.put(c19.name, c19.toString());
                map.put(c20.name, c20.toString());
                map.put(c21.name, c21.toString());
                map.put(c22.name, c22.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23> JsonCodec<TT> codec(Iso<TT, Tuple23<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple23<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15)),
                        Json.tuple(c16.name, c16.toJson(tuple._16)),
                        Json.tuple(c17.name, c17.toJson(tuple._17)),
                        Json.tuple(c18.name, c18.toJson(tuple._18)),
                        Json.tuple(c19.name, c19.toJson(tuple._19)),
                        Json.tuple(c20.name, c20.toJson(tuple._20)),
                        Json.tuple(c21.name, c21.toJson(tuple._21)),
                        Json.tuple(c22.name, c22.toJson(tuple._22)),
                        Json.tuple(c23.name, c23.toJson(tuple._23))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                DecodeResult<A16> d16 = DecodeResult.decode(object, c16.name, c16);
                DecodeResult<A17> d17 = DecodeResult.decode(object, c17.name, c17);
                DecodeResult<A18> d18 = DecodeResult.decode(object, c18.name, c18);
                DecodeResult<A19> d19 = DecodeResult.decode(object, c19.name, c19);
                DecodeResult<A20> d20 = DecodeResult.decode(object, c20.name, c20);
                DecodeResult<A21> d21 = DecodeResult.decode(object, c21.name, c21);
                DecodeResult<A22> d22 = DecodeResult.decode(object, c22.name, c22);
                DecodeResult<A23> d23 = DecodeResult.decode(object, c23.name, c23);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 ->  DecodeResult.ok(iso.reverseGet(new Tuple23<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23))))))))))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                map.put(c16.name, c16.toString());
                map.put(c17.name, c17.toString());
                map.put(c18.name, c18.toString());
                map.put(c19.name, c19.toString());
                map.put(c20.name, c20.toString());
                map.put(c21.name, c21.toString());
                map.put(c22.name, c22.toString());
                map.put(c23.name, c23.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24> JsonCodec<TT> codec(Iso<TT, Tuple24<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23, NamedJsonCodec<A24> c24) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple24<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15)),
                        Json.tuple(c16.name, c16.toJson(tuple._16)),
                        Json.tuple(c17.name, c17.toJson(tuple._17)),
                        Json.tuple(c18.name, c18.toJson(tuple._18)),
                        Json.tuple(c19.name, c19.toJson(tuple._19)),
                        Json.tuple(c20.name, c20.toJson(tuple._20)),
                        Json.tuple(c21.name, c21.toJson(tuple._21)),
                        Json.tuple(c22.name, c22.toJson(tuple._22)),
                        Json.tuple(c23.name, c23.toJson(tuple._23)),
                        Json.tuple(c24.name, c24.toJson(tuple._24))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                DecodeResult<A16> d16 = DecodeResult.decode(object, c16.name, c16);
                DecodeResult<A17> d17 = DecodeResult.decode(object, c17.name, c17);
                DecodeResult<A18> d18 = DecodeResult.decode(object, c18.name, c18);
                DecodeResult<A19> d19 = DecodeResult.decode(object, c19.name, c19);
                DecodeResult<A20> d20 = DecodeResult.decode(object, c20.name, c20);
                DecodeResult<A21> d21 = DecodeResult.decode(object, c21.name, c21);
                DecodeResult<A22> d22 = DecodeResult.decode(object, c22.name, c22);
                DecodeResult<A23> d23 = DecodeResult.decode(object, c23.name, c23);
                DecodeResult<A24> d24 = DecodeResult.decode(object, c24.name, c24);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 ->  DecodeResult.ok(iso.reverseGet(new Tuple24<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24)))))))))))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                map.put(c16.name, c16.toString());
                map.put(c17.name, c17.toString());
                map.put(c18.name, c18.toString());
                map.put(c19.name, c19.toString());
                map.put(c20.name, c20.toString());
                map.put(c21.name, c21.toString());
                map.put(c22.name, c22.toString());
                map.put(c23.name, c23.toString());
                map.put(c24.name, c24.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25> JsonCodec<TT> codec(Iso<TT, Tuple25<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23, NamedJsonCodec<A24> c24, NamedJsonCodec<A25> c25) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple25<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15)),
                        Json.tuple(c16.name, c16.toJson(tuple._16)),
                        Json.tuple(c17.name, c17.toJson(tuple._17)),
                        Json.tuple(c18.name, c18.toJson(tuple._18)),
                        Json.tuple(c19.name, c19.toJson(tuple._19)),
                        Json.tuple(c20.name, c20.toJson(tuple._20)),
                        Json.tuple(c21.name, c21.toJson(tuple._21)),
                        Json.tuple(c22.name, c22.toJson(tuple._22)),
                        Json.tuple(c23.name, c23.toJson(tuple._23)),
                        Json.tuple(c24.name, c24.toJson(tuple._24)),
                        Json.tuple(c25.name, c25.toJson(tuple._25))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                DecodeResult<A16> d16 = DecodeResult.decode(object, c16.name, c16);
                DecodeResult<A17> d17 = DecodeResult.decode(object, c17.name, c17);
                DecodeResult<A18> d18 = DecodeResult.decode(object, c18.name, c18);
                DecodeResult<A19> d19 = DecodeResult.decode(object, c19.name, c19);
                DecodeResult<A20> d20 = DecodeResult.decode(object, c20.name, c20);
                DecodeResult<A21> d21 = DecodeResult.decode(object, c21.name, c21);
                DecodeResult<A22> d22 = DecodeResult.decode(object, c22.name, c22);
                DecodeResult<A23> d23 = DecodeResult.decode(object, c23.name, c23);
                DecodeResult<A24> d24 = DecodeResult.decode(object, c24.name, c24);
                DecodeResult<A25> d25 = DecodeResult.decode(object, c25.name, c25);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 -> d25.flatMap(v25 ->  DecodeResult.ok(iso.reverseGet(new Tuple25<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25))))))))))))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                map.put(c16.name, c16.toString());
                map.put(c17.name, c17.toString());
                map.put(c18.name, c18.toString());
                map.put(c19.name, c19.toString());
                map.put(c20.name, c20.toString());
                map.put(c21.name, c21.toString());
                map.put(c22.name, c22.toString());
                map.put(c23.name, c23.toString());
                map.put(c24.name, c24.toString());
                map.put(c25.name, c25.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26> JsonCodec<TT> codec(Iso<TT, Tuple26<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23, NamedJsonCodec<A24> c24, NamedJsonCodec<A25> c25, NamedJsonCodec<A26> c26) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple26<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15)),
                        Json.tuple(c16.name, c16.toJson(tuple._16)),
                        Json.tuple(c17.name, c17.toJson(tuple._17)),
                        Json.tuple(c18.name, c18.toJson(tuple._18)),
                        Json.tuple(c19.name, c19.toJson(tuple._19)),
                        Json.tuple(c20.name, c20.toJson(tuple._20)),
                        Json.tuple(c21.name, c21.toJson(tuple._21)),
                        Json.tuple(c22.name, c22.toJson(tuple._22)),
                        Json.tuple(c23.name, c23.toJson(tuple._23)),
                        Json.tuple(c24.name, c24.toJson(tuple._24)),
                        Json.tuple(c25.name, c25.toJson(tuple._25)),
                        Json.tuple(c26.name, c26.toJson(tuple._26))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                DecodeResult<A16> d16 = DecodeResult.decode(object, c16.name, c16);
                DecodeResult<A17> d17 = DecodeResult.decode(object, c17.name, c17);
                DecodeResult<A18> d18 = DecodeResult.decode(object, c18.name, c18);
                DecodeResult<A19> d19 = DecodeResult.decode(object, c19.name, c19);
                DecodeResult<A20> d20 = DecodeResult.decode(object, c20.name, c20);
                DecodeResult<A21> d21 = DecodeResult.decode(object, c21.name, c21);
                DecodeResult<A22> d22 = DecodeResult.decode(object, c22.name, c22);
                DecodeResult<A23> d23 = DecodeResult.decode(object, c23.name, c23);
                DecodeResult<A24> d24 = DecodeResult.decode(object, c24.name, c24);
                DecodeResult<A25> d25 = DecodeResult.decode(object, c25.name, c25);
                DecodeResult<A26> d26 = DecodeResult.decode(object, c26.name, c26);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 -> d25.flatMap(v25 -> d26.flatMap(v26 ->  DecodeResult.ok(iso.reverseGet(new Tuple26<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26)))))))))))))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                map.put(c16.name, c16.toString());
                map.put(c17.name, c17.toString());
                map.put(c18.name, c18.toString());
                map.put(c19.name, c19.toString());
                map.put(c20.name, c20.toString());
                map.put(c21.name, c21.toString());
                map.put(c22.name, c22.toString());
                map.put(c23.name, c23.toString());
                map.put(c24.name, c24.toString());
                map.put(c25.name, c25.toString());
                map.put(c26.name, c26.toString());
                return "codec" + map.toString();
            }
        };
    }


    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27> JsonCodec<TT> codec(Iso<TT, Tuple27<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27>> iso, NamedJsonCodec<A1> c1, NamedJsonCodec<A2> c2, NamedJsonCodec<A3> c3, NamedJsonCodec<A4> c4, NamedJsonCodec<A5> c5, NamedJsonCodec<A6> c6, NamedJsonCodec<A7> c7, NamedJsonCodec<A8> c8, NamedJsonCodec<A9> c9, NamedJsonCodec<A10> c10, NamedJsonCodec<A11> c11, NamedJsonCodec<A12> c12, NamedJsonCodec<A13> c13, NamedJsonCodec<A14> c14, NamedJsonCodec<A15> c15, NamedJsonCodec<A16> c16, NamedJsonCodec<A17> c17, NamedJsonCodec<A18> c18, NamedJsonCodec<A19> c19, NamedJsonCodec<A20> c20, NamedJsonCodec<A21> c21, NamedJsonCodec<A22> c22, NamedJsonCodec<A23> c23, NamedJsonCodec<A24> c24, NamedJsonCodec<A25> c25, NamedJsonCodec<A26> c26, NamedJsonCodec<A27> c27) {
        return new JsonCodec<TT>() {

            @Override
            public Json.JValue toJson(TT value) {
                Tuple27<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27> tuple = iso.get(value);
                return Json.jObject(
                        Json.tuple(c1.name, c1.toJson(tuple._1)),
                        Json.tuple(c2.name, c2.toJson(tuple._2)),
                        Json.tuple(c3.name, c3.toJson(tuple._3)),
                        Json.tuple(c4.name, c4.toJson(tuple._4)),
                        Json.tuple(c5.name, c5.toJson(tuple._5)),
                        Json.tuple(c6.name, c6.toJson(tuple._6)),
                        Json.tuple(c7.name, c7.toJson(tuple._7)),
                        Json.tuple(c8.name, c8.toJson(tuple._8)),
                        Json.tuple(c9.name, c9.toJson(tuple._9)),
                        Json.tuple(c10.name, c10.toJson(tuple._10)),
                        Json.tuple(c11.name, c11.toJson(tuple._11)),
                        Json.tuple(c12.name, c12.toJson(tuple._12)),
                        Json.tuple(c13.name, c13.toJson(tuple._13)),
                        Json.tuple(c14.name, c14.toJson(tuple._14)),
                        Json.tuple(c15.name, c15.toJson(tuple._15)),
                        Json.tuple(c16.name, c16.toJson(tuple._16)),
                        Json.tuple(c17.name, c17.toJson(tuple._17)),
                        Json.tuple(c18.name, c18.toJson(tuple._18)),
                        Json.tuple(c19.name, c19.toJson(tuple._19)),
                        Json.tuple(c20.name, c20.toJson(tuple._20)),
                        Json.tuple(c21.name, c21.toJson(tuple._21)),
                        Json.tuple(c22.name, c22.toJson(tuple._22)),
                        Json.tuple(c23.name, c23.toJson(tuple._23)),
                        Json.tuple(c24.name, c24.toJson(tuple._24)),
                        Json.tuple(c25.name, c25.toJson(tuple._25)),
                        Json.tuple(c26.name, c26.toJson(tuple._26)),
                        Json.tuple(c27.name, c27.toJson(tuple._27))
                );
            }

            @Override
            public DecodeResult<TT> fromJson(Json.JValue value) {
                Json.JObject object = value.asJsonObjectOrEmpty();
                DecodeResult<A1> d1 = DecodeResult.decode(object, c1.name, c1);
                DecodeResult<A2> d2 = DecodeResult.decode(object, c2.name, c2);
                DecodeResult<A3> d3 = DecodeResult.decode(object, c3.name, c3);
                DecodeResult<A4> d4 = DecodeResult.decode(object, c4.name, c4);
                DecodeResult<A5> d5 = DecodeResult.decode(object, c5.name, c5);
                DecodeResult<A6> d6 = DecodeResult.decode(object, c6.name, c6);
                DecodeResult<A7> d7 = DecodeResult.decode(object, c7.name, c7);
                DecodeResult<A8> d8 = DecodeResult.decode(object, c8.name, c8);
                DecodeResult<A9> d9 = DecodeResult.decode(object, c9.name, c9);
                DecodeResult<A10> d10 = DecodeResult.decode(object, c10.name, c10);
                DecodeResult<A11> d11 = DecodeResult.decode(object, c11.name, c11);
                DecodeResult<A12> d12 = DecodeResult.decode(object, c12.name, c12);
                DecodeResult<A13> d13 = DecodeResult.decode(object, c13.name, c13);
                DecodeResult<A14> d14 = DecodeResult.decode(object, c14.name, c14);
                DecodeResult<A15> d15 = DecodeResult.decode(object, c15.name, c15);
                DecodeResult<A16> d16 = DecodeResult.decode(object, c16.name, c16);
                DecodeResult<A17> d17 = DecodeResult.decode(object, c17.name, c17);
                DecodeResult<A18> d18 = DecodeResult.decode(object, c18.name, c18);
                DecodeResult<A19> d19 = DecodeResult.decode(object, c19.name, c19);
                DecodeResult<A20> d20 = DecodeResult.decode(object, c20.name, c20);
                DecodeResult<A21> d21 = DecodeResult.decode(object, c21.name, c21);
                DecodeResult<A22> d22 = DecodeResult.decode(object, c22.name, c22);
                DecodeResult<A23> d23 = DecodeResult.decode(object, c23.name, c23);
                DecodeResult<A24> d24 = DecodeResult.decode(object, c24.name, c24);
                DecodeResult<A25> d25 = DecodeResult.decode(object, c25.name, c25);
                DecodeResult<A26> d26 = DecodeResult.decode(object, c26.name, c26);
                DecodeResult<A27> d27 = DecodeResult.decode(object, c27.name, c27);
                return d1.flatMap(v1 -> d2.flatMap(v2 -> d3.flatMap(v3 -> d4.flatMap(v4 -> d5.flatMap(v5 -> d6.flatMap(v6 -> d7.flatMap(v7 -> d8.flatMap(v8 -> d9.flatMap(v9 -> d10.flatMap(v10 -> d11.flatMap(v11 -> d12.flatMap(v12 -> d13.flatMap(v13 -> d14.flatMap(v14 -> d15.flatMap(v15 -> d16.flatMap(v16 -> d17.flatMap(v17 -> d18.flatMap(v18 -> d19.flatMap(v19 -> d20.flatMap(v20 -> d21.flatMap(v21 -> d22.flatMap(v22 -> d23.flatMap(v23 -> d24.flatMap(v24 -> d25.flatMap(v25 -> d26.flatMap(v26 -> d27.flatMap(v27 ->  DecodeResult.ok(iso.reverseGet(new Tuple27<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22, v23, v24, v25, v26, v27))))))))))))))))))))))))))))));
            }

            @Override
            public String toString() {
                Map<String, String> map = new HashMap<>();
                map.put(c1.name, c1.toString());
                map.put(c2.name, c2.toString());
                map.put(c3.name, c3.toString());
                map.put(c4.name, c4.toString());
                map.put(c5.name, c5.toString());
                map.put(c6.name, c6.toString());
                map.put(c7.name, c7.toString());
                map.put(c8.name, c8.toString());
                map.put(c9.name, c9.toString());
                map.put(c10.name, c10.toString());
                map.put(c11.name, c11.toString());
                map.put(c12.name, c12.toString());
                map.put(c13.name, c13.toString());
                map.put(c14.name, c14.toString());
                map.put(c15.name, c15.toString());
                map.put(c16.name, c16.toString());
                map.put(c17.name, c17.toString());
                map.put(c18.name, c18.toString());
                map.put(c19.name, c19.toString());
                map.put(c20.name, c20.toString());
                map.put(c21.name, c21.toString());
                map.put(c22.name, c22.toString());
                map.put(c23.name, c23.toString());
                map.put(c24.name, c24.toString());
                map.put(c25.name, c25.toString());
                map.put(c26.name, c26.toString());
                map.put(c27.name, c27.toString());
                return "codec" + map.toString();
            }
        };
    }
}
