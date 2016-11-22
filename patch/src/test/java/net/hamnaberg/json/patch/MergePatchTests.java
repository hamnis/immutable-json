package net.hamnaberg.json.patch;

import net.hamnaberg.json.Json;
import net.hamnaberg.json.jackson.JacksonStreamingParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class MergePatchTests {
    private final Json.JValue document;
    private final Json.JValue patch;
    private final Json.JValue result;

    @Parameterized.Parameters()
    public static Collection<Object[]> data() throws Exception {
        InputStream stream = RFC9602Test.class.getResourceAsStream("/rfc7396/spec_tests.json");
        return buildTestSpec(stream);
    }

    static Collection<Object[]> buildTestSpec(InputStream stream) {
        Json.JValue value = new JacksonStreamingParser().parse(stream);
        Json.JArray tests = value.asJsonArrayOrEmpty();
        return tests.mapToList(j -> {
            Json.JObject object = j.asJsonObjectOrEmpty();
            Json.JValue document = object.get("doc").get();
            Json.JValue patch = object.get("patch").get();
            Json.JValue result = object.get("result").get();
            return new Object[]{ document, patch, result};
        }).toJavaList();
    }

    public MergePatchTests(Json.JValue document, Json.JValue patch, Json.JValue result) {
        this.document = document;
        this.patch = patch;
        this.result = result;
    }

    @Test
    public void patch() {
        Json.JValue patched = MergePatch.patch(document, this.patch);
        assertEquals("Patched did not match result", result, patched);
    }
}
