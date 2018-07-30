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
        if (isType(obj, Account.Checking.class)) {
            return DecodeResult.fromOption(obj.getAsBigDecimal("amount")).map(Account.Checking::new);
        }
        return DecodeResult.fail("Not a checking account");
    }, it -> jsonOf(Account.Checking.class, it.amount)
    );

    private JsonCodec<Account.Standard> standardCodec = JsonCodec.lift((c) -> {
        Json.JObject obj = c.asJsonObjectOrEmpty();
        if (isType(obj, Account.Standard.class)) {
            return DecodeResult.fromOption(obj.getAsBigDecimal("amount")).map(Account.Standard::new);
        }
        return DecodeResult.fail("Not a standard account");
    }, it -> jsonOf(Account.Standard.class, it.amount)
    );


    private final ADTCodec<Account> adtCodec = new ADTCodec<>(Account.class, HashMap.of(
            Account.Checking.class, checkingCodec,
            Account.Standard.class, standardCodec
    ));


    @Test
    public void testStandardDecode() {
        DecodeResult<Account> result = adtCodec.fromJson(jsonOf(Account.Standard.class, BigDecimal.valueOf(0)));
        assertTrue(result.unsafeGet() instanceof Account.Standard);
        assertEquals(result.unsafeGet().amount, BigDecimal.ZERO);
    }

    @Test
    public void testCheckingDecode() {
        DecodeResult<Account> result = adtCodec.fromJson(jsonOf(Account.Checking.class, BigDecimal.valueOf(0)));
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
        Json.JObject expected = jsonOf(Account.Standard.class, BigDecimal.valueOf(123));
        Json.JValue json = adtCodec.toJson(new Account.Standard(BigDecimal.valueOf(123L)));
        assertEquals("Unexpected json", expected, json);
    }

    @Test
    public void encodeChecking() {
        Json.JObject expected = jsonOf(Account.Checking.class, BigDecimal.valueOf(123));

        Json.JValue json = adtCodec.toJson(new Account.Checking(BigDecimal.valueOf(123L)));
        assertEquals("Unexpected json", expected, json);
    }

    private Json.JObject jsonOf(Class<? extends Account> type, BigDecimal amount) {
        return Json.jObject(HashMap.of(
                "type", Json.jString(type.getSimpleName()),
                "amount", Json.jNumber(amount)
        ));
    }

    private boolean isType(Json.JObject obj, Class<?> clazz) {
        return obj.getAsString("type").exists(clazz.getSimpleName()::equals);
    }
}
