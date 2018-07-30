package net.hamnaberg.json.codec;

import io.vavr.collection.HashMap;
import net.hamnaberg.json.Json;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ADTCodecTest {


    public static abstract class Account {
        public final BigDecimal amount;

        private Account(BigDecimal amount) {
            this.amount = amount;
        }

        public static class Checking extends Account {

            public Checking(BigDecimal amount) {
                super(amount);
            }
        }

        public static class Standard extends Account {
            public Standard(BigDecimal amount) {
                super(amount);
            }
        }
    }

    private JsonCodec<Account.Checking> checkingCodec = JsonCodec.lift((c) -> {
        Json.JObject obj = c.asJsonObjectOrEmpty();
        if (obj.getAsString("type").exists("checking"::equals)) {
            return DecodeResult.fromOption(obj.getAsBigDecimal("amount")).map(Account.Checking::new);
        }
        return DecodeResult.fail("Not a checking account");
    }, it -> Json.jObject(
            HashMap.of(
                    "amount", Json.jNumber(it.amount),
                    "type", Json.jString("checking")
            )
    ));

    private JsonCodec<Account.Standard> standardCodec = JsonCodec.lift((c) -> {
        Json.JObject obj = c.asJsonObjectOrEmpty();
        if (obj.getAsString("type").exists("standard"::equals)) {
            return DecodeResult.fromOption(obj.getAsBigDecimal("amount")).map(Account.Standard::new);
        }
        return DecodeResult.fail("Not a standard account");
    }, it -> Json.jObject(
            HashMap.of(
                    "amount", Json.jNumber(it.amount),
                    "type", Json.jString("standard")
            )
    ));


    private final ADTCodec<Account> adtCodec = new ADTCodec<>(Account.class, HashMap.of(
            Account.Checking.class, checkingCodec,
            Account.Standard.class, standardCodec
    ));


    @Test
    public void testStandardDecode() {
        DecodeResult<Account> result = adtCodec.fromJson(Json.jObject(HashMap.of(
                "type", Json.jString("standard"),
                "amount", Json.jNumber(0)
        )));
        assertTrue(result.unsafeGet() instanceof Account.Standard);
        assertEquals(result.unsafeGet().amount, BigDecimal.ZERO);
    }

    @Test
    public void testCheckingDecode() {
        DecodeResult<Account> result = adtCodec.fromJson(Json.jObject(HashMap.of(
                "type", Json.jString("checking"),
                "amount", Json.jNumber(0)
        )));
        assertTrue(result.unsafeGet() instanceof Account.Checking);
        assertEquals(result.unsafeGet().amount, BigDecimal.ZERO);
    }

    @Test
    public void testBothFail() {
        DecodeResult<Account> result = adtCodec.fromJson(Json.jObject(HashMap.of(
                "type", Json.jString("unknown"),
                "amount", Json.jNumber(123)
        )));
        assertTrue(result.isFailure());
    }


    @Test
    public void encodeStandard() {
        Json.JObject expected = Json.jObject(HashMap.of(
                "type", Json.jString("standard"),
                "amount", Json.jNumber(123)
        ));
        Json.JValue json = adtCodec.toJson(new Account.Standard(BigDecimal.valueOf(123L)));
        assertEquals("Unexpected json", expected, json);
    }

    @Test
    public void encodeChecking() {
        Json.JObject expected = Json.jObject(HashMap.of(
                "type", Json.jString("checking"),
                "amount", Json.jNumber(123)
        ));
        Json.JValue json = adtCodec.toJson(new Account.Checking(BigDecimal.valueOf(123L)));
        assertEquals("Unexpected json", expected, json);
    }
}
