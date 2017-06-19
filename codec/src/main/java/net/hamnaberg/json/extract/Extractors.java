package net.hamnaberg.json.extract;

import io.vavr.*;
import net.hamnaberg.json.codec.Decoders;
import net.hamnaberg.json.util.*;

@Deprecated
public abstract class Extractors {
    private Extractors() {
    }

    public static <TT, A> Extractor<TT> extract(TypedField<A> f1, Function1<A, TT> func) {
        return (object) -> Decoders.decode(f1.toFieldDecoder(), func).fromJson(object);
    }

    public static <TT, A1, A2> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, Function2<A1, A2, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, Function3<A1, A2, A3, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, Function4<A1, A2, A3, A4, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, Function5<A1, A2, A3, A4, A5, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, Function6<A1, A2, A3, A4, A5, A6, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, Function7<A1, A2, A3, A4, A5, A6, A7, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, Function8<A1, A2, A3, A4, A5, A6, A7, A8, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, F9<A1, A2, A3, A4, A5, A6, A7, A8, A9, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, F10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, F11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, F12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, F13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, F14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, F15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, F16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), tf16.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, F17<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), tf16.toFieldDecoder(), tf17.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, F18<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), tf16.toFieldDecoder(), tf17.toFieldDecoder(), tf18.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, F19<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), tf16.toFieldDecoder(), tf17.toFieldDecoder(), tf18.toFieldDecoder(), tf19.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, F20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), tf16.toFieldDecoder(), tf17.toFieldDecoder(), tf18.toFieldDecoder(), tf19.toFieldDecoder(), tf20.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, F21<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), tf16.toFieldDecoder(), tf17.toFieldDecoder(), tf18.toFieldDecoder(), tf19.toFieldDecoder(), tf20.toFieldDecoder(), tf21.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, TypedField<A22> tf22, F22<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), tf16.toFieldDecoder(), tf17.toFieldDecoder(), tf18.toFieldDecoder(), tf19.toFieldDecoder(), tf20.toFieldDecoder(), tf21.toFieldDecoder(), tf22.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, TypedField<A22> tf22, TypedField<A23> tf23, F23<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), tf16.toFieldDecoder(), tf17.toFieldDecoder(), tf18.toFieldDecoder(), tf19.toFieldDecoder(), tf20.toFieldDecoder(), tf21.toFieldDecoder(), tf22.toFieldDecoder(), tf23.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, TypedField<A22> tf22, TypedField<A23> tf23, TypedField<A24> tf24, F24<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), tf16.toFieldDecoder(), tf17.toFieldDecoder(), tf18.toFieldDecoder(), tf19.toFieldDecoder(), tf20.toFieldDecoder(), tf21.toFieldDecoder(), tf22.toFieldDecoder(), tf23.toFieldDecoder(), tf24.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, TypedField<A22> tf22, TypedField<A23> tf23, TypedField<A24> tf24, TypedField<A25> tf25, F25<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), tf16.toFieldDecoder(), tf17.toFieldDecoder(), tf18.toFieldDecoder(), tf19.toFieldDecoder(), tf20.toFieldDecoder(), tf21.toFieldDecoder(), tf22.toFieldDecoder(), tf23.toFieldDecoder(), tf24.toFieldDecoder(), tf25.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, TypedField<A22> tf22, TypedField<A23> tf23, TypedField<A24> tf24, TypedField<A25> tf25, TypedField<A26> tf26, F26<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), tf16.toFieldDecoder(), tf17.toFieldDecoder(), tf18.toFieldDecoder(), tf19.toFieldDecoder(), tf20.toFieldDecoder(), tf21.toFieldDecoder(), tf22.toFieldDecoder(), tf23.toFieldDecoder(), tf24.toFieldDecoder(), tf25.toFieldDecoder(), tf26.toFieldDecoder(), func).fromJson(object);
        };
    }

    public static <TT, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27> Extractor<TT> extract(TypedField<A1> tf1, TypedField<A2> tf2, TypedField<A3> tf3, TypedField<A4> tf4, TypedField<A5> tf5, TypedField<A6> tf6, TypedField<A7> tf7, TypedField<A8> tf8, TypedField<A9> tf9, TypedField<A10> tf10, TypedField<A11> tf11, TypedField<A12> tf12, TypedField<A13> tf13, TypedField<A14> tf14, TypedField<A15> tf15, TypedField<A16> tf16, TypedField<A17> tf17, TypedField<A18> tf18, TypedField<A19> tf19, TypedField<A20> tf20, TypedField<A21> tf21, TypedField<A22> tf22, TypedField<A23> tf23, TypedField<A24> tf24, TypedField<A25> tf25, TypedField<A26> tf26, TypedField<A27> tf27, F27<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24, A25, A26, A27, TT> func) {
        return (object) -> {
            return Decoders.decode(tf1.toFieldDecoder(), tf2.toFieldDecoder(), tf3.toFieldDecoder(), tf4.toFieldDecoder(), tf5.toFieldDecoder(), tf6.toFieldDecoder(), tf7.toFieldDecoder(), tf8.toFieldDecoder(), tf9.toFieldDecoder(), tf10.toFieldDecoder(), tf11.toFieldDecoder(), tf12.toFieldDecoder(), tf13.toFieldDecoder(), tf14.toFieldDecoder(), tf15.toFieldDecoder(), tf16.toFieldDecoder(), tf17.toFieldDecoder(), tf18.toFieldDecoder(), tf19.toFieldDecoder(), tf20.toFieldDecoder(), tf21.toFieldDecoder(), tf22.toFieldDecoder(), tf23.toFieldDecoder(), tf24.toFieldDecoder(), tf25.toFieldDecoder(), tf26.toFieldDecoder(), tf27.toFieldDecoder(), func).fromJson(object);
        };
    }
}
