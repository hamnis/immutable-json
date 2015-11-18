package net.hamnaberg.json;

import net.hamnaberg.json.io.JacksonStreamingParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class JsonFromRepoTest {


    private final String name;
    private final Json.JValue document;
    private final JsonPatch patch;
    private final Optional<Json.JValue> expected;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() throws Exception {
        InputStream stream = JsonFromRepoTest.class.getResourceAsStream("/tests.json");
        return RFC9602Test.buildTestSpec(stream);
    }


    public JsonFromRepoTest(String name, Json.JValue document, JsonPatch patch, Optional<Json.JValue> expected) {
        this.name = name;
        this.document = document;
        this.patch = patch;
        this.expected = expected;
    }


    @Test
    public void runTest() {
        try {
            Json.JValue applied = patch.apply(document);
            assertNotNull("Document was null", applied);
            if (expected.isPresent()) {
                assertEquals(expected.get(), applied);
            }
        } catch (IllegalStateException e) {
            if (expected.isPresent()) {
                fail(e.getMessage());
            }
        }
    }
}
