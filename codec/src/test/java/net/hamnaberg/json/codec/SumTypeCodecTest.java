package net.hamnaberg.json.codec;

import net.hamnaberg.json.Json;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SumTypeCodecTest {


    static abstract class Account {
        final BigDecimal amount;

        private Account(BigDecimal amount) {
            this.amount = amount;
        }

        static class Checking extends Account {

            Checking(BigDecimal amount) {
                super(amount);
            }
        }

        static class Standard extends Account {
            Standard(BigDecimal amount) {
                super(amount);
            }
        }
    }

    private JsonCodec<Account.Checking> checkingCodec = JsonCodec.lift(
            Decoders.decode(FieldDecoder.typedFieldOf("amount", Decoders.DBigDecimal), Account.Checking::new),
            it -> jsonOf(Account.Checking.class, it.amount)
    );

    private JsonCodec<Account.Standard> standardCodec = JsonCodec.lift(
            Decoders.decode(FieldDecoder.typedFieldOf("amount", Decoders.DBigDecimal), Account.Standard::new),
            it -> jsonOf(Account.Standard.class, it.amount)
    );


    private final SumTypeCodec<Account> sumTypeCodec = new SumTypeCodec<>(Account.class, Map.of(
            Account.Checking.class, checkingCodec,
            Account.Standard.class, standardCodec
    ));


    @Test
    public void testStandardDecode() {
        DecodeResult<Account> result = sumTypeCodec.fromJson(jsonOf(Account.Standard.class, BigDecimal.valueOf(0)));
        assertTrue(result.unsafeGet() instanceof Account.Standard);
        assertEquals(result.unsafeGet().amount, BigDecimal.ZERO);
    }

    @Test
    public void testCheckingDecode() {
        DecodeResult<Account> result = sumTypeCodec.fromJson(jsonOf(Account.Checking.class, BigDecimal.valueOf(0)));
        assertTrue(result.unsafeGet() instanceof Account.Checking);
        assertEquals(result.unsafeGet().amount, BigDecimal.ZERO);
    }

    @Test
    public void testBothFail() {
        DecodeResult<Account> result = sumTypeCodec.fromJson(Json.jObject(
                "type", Json.jString("unknown"),
                "amount", Json.jNumber(123)
        ));
        assertTrue(result.isFailure());
    }


    @Test
    public void encodeStandard() {
        Json.JObject expected = jsonOf(Account.Standard.class, BigDecimal.valueOf(123));
        Json.JValue json = sumTypeCodec.toJson(new Account.Standard(BigDecimal.valueOf(123L)));
        assertEquals("Unexpected json", expected, json);
    }

    @Test
    public void encodeChecking() {
        Json.JObject expected = jsonOf(Account.Checking.class, BigDecimal.valueOf(123));

        Json.JValue json = sumTypeCodec.toJson(new Account.Checking(BigDecimal.valueOf(123L)));
        assertEquals("Unexpected json", expected, json);
    }

    private Json.JObject jsonOf(Class<? extends Account> type, BigDecimal amount) {
        return Json.jObject(
                "type", Json.jString(type.getSimpleName().toLowerCase()),
                "amount", Json.jNumber(amount)
        );
    }
}
