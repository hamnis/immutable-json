
package org.glassfish.json;

import javax.json.JsonNumber;
import javax.json.JsonString;
import java.math.BigDecimal;

//Factory class to avoid suckiness in underlying library.
public abstract class JsonFactory {
    private JsonFactory(){}

    public static JsonString jsonString(String s) {
        return new JsonStringImpl(s);
    }

    public static JsonNumber jsonNumber(BigDecimal s) {
        return JsonNumberImpl.getJsonNumber(s);
    }
}
