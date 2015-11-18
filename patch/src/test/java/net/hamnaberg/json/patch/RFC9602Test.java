package net.hamnaberg.json.patch;

import net.hamnaberg.json.Json;
import net.hamnaberg.json.io.JacksonStreamingParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class RFC9602Test {


    private final String name;
    private final Json.JValue document;
    private final Json.JArray patch;
    private final Optional<Json.JValue> expected;

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
            Optional<Json.JValue> expected = object.get("expected");
            String comment = object.getAsStringOrEmpty("comment");
            Json.JArray patchArray = object.getAsArrayOrEmpty("patch");
            return new Object[]{ comment, document, patchArray, expected};
        });
    }


    public RFC9602Test(String name, Json.JValue document, Json.JArray patch, Optional<Json.JValue> expected) {
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
            if (expected.isPresent()) {
                assertEquals(expected.get(), applied);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            if (expected.isPresent()) {
                fail(e.getMessage());
            }
        }
    }
}
