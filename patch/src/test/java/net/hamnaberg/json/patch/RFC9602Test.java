package net.hamnaberg.json.patch;

import io.vavr.control.Option;
import net.hamnaberg.json.Json;
import net.hamnaberg.json.jackson.JacksonStreamingParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.util.Collection;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class RFC9602Test {


    private final String name;
    private final Json.JValue document;
    private final Json.JArray patch;
    private final Option<Json.JValue> expected;

    @Parameterized.Parameters(name = "spec - {0}")
    public static Collection<Object[]> data() throws Exception {
        InputStream stream = RFC9602Test.class.getResourceAsStream("/spec_tests.json");
        return buildTestSpec(stream);
    }

    static Collection<Object[]> buildTestSpec(InputStream stream) {
        Json.JValue value = new JacksonStreamingParser().parse(stream);
        Json.JArray tests = value.asJsonArrayOrEmpty();
        return tests.mapToList(j -> {
            Json.JObject object = j.asJsonObjectOrEmpty();
            Json.JValue document = object.get("doc").get();
            Option<Json.JValue> expected = object.get("expected");
            String comment = object.getAsStringOrEmpty("comment");
            Json.JArray patchArray = object.getAsArrayOrEmpty("patch");
            return new Object[]{ comment, document, patchArray, expected};
        }).toJavaList();
    }


    public RFC9602Test(String name, Json.JValue document, Json.JArray patch, Option<Json.JValue> expected) {
        this.name = name;
        this.document = document;
        this.patch = patch;
        this.expected = expected;
    }


    @Test
    public void runTest() {
        try {
            JsonPatch patch = JsonPatch.fromArray(this.patch);
            Json.JValue applied = patch.apply(document);
            assertNotNull("Document was null", applied);
            if (expected.isDefined()) {
                assertEquals(expected.get(), applied);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            if (expected.isDefined()) {
                fail(e.getMessage());
            }
        }
    }
}
