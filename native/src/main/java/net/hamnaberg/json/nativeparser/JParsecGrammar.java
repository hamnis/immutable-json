package net.hamnaberg.json.nativeparser;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.io.JsonParseException;
import org.jparsec.*;
import org.jparsec.pattern.Patterns;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class JParsecGrammar {
    static class JSONStringLiteral {
        private final static int[] HEX = new int[128];
        static {
            Arrays.fill(HEX, -1);
            for (int i='0'; i <= '9'; i++) {
                HEX[i] = i-'0';
            }
            for (int i='A'; i <= 'F'; i++) {
                HEX[i] = 10+i-'A';
            }
            for (int i='a'; i <= 'f'; i++) {
                HEX[i] = 10+i-'a';
            }
        }
        private final static int HEX_LENGTH = HEX.length;

        private static char escapedChar(char c) {
            switch (c) {
                case 'b':
                    return '\b';
                case 'f':
                    return '\f';
                case 'r':
                    return '\r';
                case 'n':
                    return '\n';
                case 't':
                    return '\t';
                default:
                    return c;
            }
        }

        static String tokenizeDoubleQuote(String text) {
            final int end = text.length() - 1;
            final StringBuilder buf = new StringBuilder();
            for (int i = 1; i < end; i++) {
                char c = text.charAt(i);
                if (c != '\\') {
                    buf.append(c);
                }
                else {
                    char c1 = text.charAt(++i);
                    if (c1 == 'u') {
                        int unicode = c1;
                        if (i+4 <= end) {
                            for (int j = i+1; j < (i + 5); j++) {
                                char ch3 = text.charAt(j);
                                int digit = (ch3 >= 0 && ch3 < HEX_LENGTH) ? HEX[ch3] : -1;
                                if (digit < 0) {
                                    throw new JsonParseException("Unxpected character: " + ch3);
                                }
                                unicode = (unicode << 4)|digit;
                            }
                            buf.append((char)unicode);
                            i += 4;
                        } else {
                            throw new JsonParseException("Expected unicode escape but was " + c1);
                        }

                    } else {
                        buf.append(escapedChar(c1));
                    }
                }
            }
            return buf.toString();
        }

        static Parser<String> DOUBLE_QUOTE_TOKENIZER = Scanners.DOUBLE_QUOTE_STRING.map(JSONStringLiteral::tokenizeDoubleQuote);
    }


    private static Parser<BigDecimal> bigDecimalTokenizer() {
        Parser<String> BDScanner = Patterns.regex("(-?(0|[1-9][0-9]*)([.][0-9]+)?([eE][+-]?[0-9]*)?)").toScanner("bigdecimal").source();
        Parser<BigDecimal> BDTokenizer = BDScanner.map(BigDecimal::new).label("bigdecimal tokenizer");
        Parser<BigDecimal> HexScanner = Terminals.LongLiteral.HEX_TOKENIZER.map(BigDecimal::valueOf);

        return Parsers.or(BDTokenizer, HexScanner);
    }

    private static final Parser<BigDecimal> numericTokenizer = bigDecimalTokenizer();

    private static List<String> OPERATORS = Arrays.asList(
            "{", "[", "}", "]", ",", ":"
    );


    private static List<String> KEYWORDS = Arrays.asList(
            "null", "true", "false"
    );

    private static Terminals TERMS =
        Terminals.operators(OPERATORS).words(Scanners.IDENTIFIER).keywords(KEYWORDS).build();

    private static final Parser<Void> WHITESPACE = Scanners.WHITESPACES.skipMany();


    static final Parser<?> TOKENIZER = Parsers.or(JSONStringLiteral.DOUBLE_QUOTE_TOKENIZER, numericTokenizer, TERMS.tokenizer());


    private static Parser<?> term(String term) {
        return TERMS.token(term);
    }

    private static Parser<BigDecimal> bdParser = Parsers.tokenType(BigDecimal.class, "numeric literal");

    private static Parser<Json.JBoolean> boolParser = term("true").retn(Json.jBoolean(true)).or(term("false").retn(Json.jBoolean(false)));

    private static Parser<Json.JNull> nullParser = term("null").retn(Json.jNull());

    private static Parser<Json.JString> stringParser = Terminals.StringLiteral.PARSER.map(Json::jString);

    private static Parser<Json.JNumber> numberParser = bdParser.map(Json::jNumber);

    private static Parser.Reference<Json.JValue> jsonParser = Parser.newReference();

    private static final Parser<Json.JValue> LazyParser = jsonParser.lazy();

    private static Parser<Tuple2<String, Json.JValue>> propertyValue = Terminals.StringLiteral.PARSER.next(s -> term(":").next(LazyParser).next(v -> Parsers.constant(Tuple.of(s, v))));

    private static Parser<Json.JObject> objectParser = Parsers.between(term("{").label("object start"), propertyValue.sepBy(term(",")), term("}").label("object end")).map(Json::jObject);

    private static Parser<Json.JArray> arrayParser = Parsers.between(term("["), LazyParser.sepBy(term(",")), term("]")).map(Json::jArray).label("array");

    static {
        jsonParser.set(
                Parsers.or(
                        boolParser,
                        nullParser,
                        stringParser,
                        numberParser,
                        arrayParser,
                        objectParser
                ).label("Json")
        );
    }

    static Parser<Json.JValue> parser = jsonParser.lazy().from(TOKENIZER, WHITESPACE);
}
