package net.hamnaberg.json;

import io.vavr.collection.List;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static net.hamnaberg.json.Json.*;
import static org.junit.Assert.assertThat;

public class PrettyPrintTest {

    @Test
    public void dropNullKeys() throws Exception {
        JObject object = jObject(List.of(
                tuple("Foo", "bar"),
                tuple("nullable", jNull()),
                tuple("meh", 12)
        ));

        String nospaces = object.nospaces();
        assertThat("Did not contain null", nospaces, CoreMatchers.containsString("\"nullable\":null"));
        String noNull = object.pretty(PrettyPrinter.nospaces().dropNullKeys(true));
        assertThat("Contained null", noNull, CoreMatchers.not(CoreMatchers.containsString("\"nullable\":null")));
    }
}
