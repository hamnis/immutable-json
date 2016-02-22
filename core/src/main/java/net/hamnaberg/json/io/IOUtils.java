package net.hamnaberg.json.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by erlhamna on 19/02/16.
 */
public class IOUtils {

    public static String toString(InputStream stream) throws IOException {
        return toString(new InputStreamReader(stream));
    }

    public static String toString(Reader reader) throws IOException {
        char[] arr = new char[8 * 1024];
        StringBuilder buffer = new StringBuilder();
        int numCharsRead;
        try(Reader r = reader) {
            while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
                buffer.append(arr, 0, numCharsRead);
            }
        }

        return buffer.toString();
    }
}
