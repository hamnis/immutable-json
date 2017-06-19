
package net.hamnaberg.json.util;

import io.vavr.collection.List;

public final class Tuple20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20> {
    public final A1 _1;
    public final A2 _2;
    public final A3 _3;
    public final A4 _4;
    public final A5 _5;
    public final A6 _6;
    public final A7 _7;
    public final A8 _8;
    public final A9 _9;
    public final A10 _10;
    public final A11 _11;
    public final A12 _12;
    public final A13 _13;
    public final A14 _14;
    public final A15 _15;
    public final A16 _16;
    public final A17 _17;
    public final A18 _18;
    public final A19 _19;
    public final A20 _20;

    public Tuple20(A1 _1, A2 _2, A3 _3, A4 _4, A5 _5, A6 _6, A7 _7, A8 _8, A9 _9, A10 _10, A11 _11, A12 _12, A13 _13, A14 _14, A15 _15, A16 _16, A17 _17, A18 _18, A19 _19, A20 _20) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
        this._5 = _5;
        this._6 = _6;
        this._7 = _7;
        this._8 = _8;
        this._9 = _9;
        this._10 = _10;
        this._11 = _11;
        this._12 = _12;
        this._13 = _13;
        this._14 = _14;
        this._15 = _15;
        this._16 = _16;
        this._17 = _17;
        this._18 = _18;
        this._19 = _19;
        this._20 = _20;
    }

    public <B> B transform(F20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, B> f) {
        return f.apply(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19, _20);
    }

    private List<?> toList() {
        return List.of(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19, _20);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple20 tuple = (Tuple20) o;

        return !toList().equals(tuple.toList());
    }

    @Override
    public int hashCode() {
        return toList().hashCode();
    }
}
