/**
 * Copyright 2014-2015, Jon Hanson. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * This software is provided by the copyright holders "as is" and any express or
 * implied warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose are disclaimed. In no
 * event shall the copyright holders be liable for any direct, indirect,
 * incidental, special, exemplary, or consequential damages (including, but not
 * limited to, procurement of substitute goods or services; loss of use, data,
 * or profits; or business interruption) however caused and on any theory of
 * liability, whether in contract, strict liability, or tort (including
 * negligence or otherwise) arising in any way out of the use of this software,
 * even if advised of the possibility of such damage.
 */
package net.hamnaberg.json.nativeparser;

import io.vavr.Tuple2;
import org.javafp.data.*;
import org.javafp.parsecj.*;

import java.math.BigDecimal;

import static org.javafp.parsecj.Combinators.*;
import static org.javafp.parsecj.Text.*;
import static net.hamnaberg.json.Json.*;

/**
 * A grammar for JSON.
 * Copied and adapted from https://github.com/jon-hanson/parsecj/blob/master/src/test/java/org/javafp/parsecj/json/Grammar.java
 *
 */
class Grammar {
    private static <T> Parser<Character, T> tok(Parser<Character, T> p) {
        return p.bind(x -> wspaces.then(retn(x)));
    }

    private static final Parser.Ref<Character, JValue> jvalue = Parser.ref();

    private static final Parser<Character, JValue> jnull = tok(string("null")).then(retn(jNull().asJValue())).label("null");

    private static final Parser<Character, Boolean> jtrue = tok(string("true").then(retn(Boolean.TRUE)));
    private static final Parser<Character, Boolean> jfalse = tok(string("false").then(retn(Boolean.FALSE)));

    private static final Parser<Character, JValue> jbool = tok(jtrue.or(jfalse).bind(b -> retn(jBoolean(b).asJValue()))).label("boolean");

    private static final Parser<Character, BigDecimal> bigdecimal =
            bind(
                    regex("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?"),
                    s -> retn(new BigDecimal(s))
            ).label("bigdecimal");

    private static final Parser<Character, JValue> jnumber = tok(bigdecimal.bind(d -> retn(jNumber(d).asJValue()))).label("number");

    private static final Parser<Character, Byte> hexDigit =
            satisfy((Character c) -> Character.digit(c, 16) != -1)
                    .bind(c -> retn((byte) Character.digit(c, 16))).label("hex digit");

    private static final Parser<Character, Character> uni =
            hexDigit.bind(d0 ->
                    hexDigit.bind(d1 ->
                            hexDigit.bind(d2 ->
                                    hexDigit.bind(d3 ->
                                            retn((d0<<0x3) & (d1<<0x2) & (d2<<0x1) & d0)
                                    )
                            )
                    )
            ).bind(i -> retn((char) i.intValue()));

    private static final Parser<Character, Character> esc =
            choice(
                    chr('"'),
                    chr('\\'),
                    chr('/'),
                    chr('b').then(retn('\b')),
                    chr('f').then(retn('\f')),
                    chr('n').then(retn('\n')),
                    chr('r').then(retn('\r')),
                    chr('t').then(retn('\t')),
                    chr('u').then(uni)
            ).label("escape character");

    private static final Parser<Character, Character> stringChar =
            (
                    chr('\\').then(esc)
            ).or(
                    satisfy(c -> c != '"' && c != '\\')
            );

    private static final Parser<Character, String> jstring =
            tok(between(
                    chr('"'),
                    chr('"'),
                    many(stringChar).bind(l -> retn(IList.listToString(l)))
            )).label("string");

    private static final Parser<Character, JValue> jtext =
            jstring.bind(s ->
                    retn(jString(s).asJValue())
            ).label("text");

    private static final Parser<Character, JValue> jarray =
            between(
                    tok(chr('[')),
                    tok(chr(']')),
                    sepBy(
                            jvalue,
                            tok(chr(','))
                    )
            ).bind(l ->
                    retn(jArray(IList.toList(l)).asJValue())
            ).label("array");

    private static final Parser<Character, Tuple2<String, JValue>> jfield =
            jstring.bind(name ->
                    tok(chr(':'))
                            .then(jvalue)
                            .bind(value ->
                                    retn(tuple(name, value))
                            )
            );

    private static final Parser<Character, JValue> jobject =
            between(
                    tok(chr('{')),
                    tok(chr('}')),
                    sepBy(
                            jfield,
                            tok(chr(','))
                    ).bind(lf -> retn(jObject(lf).asJValue()))
            ).label("object");

    static {
        jvalue.set(
                choice(
                        jnull,
                        jbool,
                        jnumber,
                        jtext,
                        jarray,
                        jobject
                ).label("JSON value")
        );
    }

    private static final Parser<Character, JValue> parser = wspaces.then(jvalue);

    static Reply<Character, JValue> parse(String str) {
        return parser.parse(State.of(str));
    }
}
