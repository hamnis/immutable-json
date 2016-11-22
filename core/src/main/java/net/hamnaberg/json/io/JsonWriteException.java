package net.hamnaberg.json.io;


public class JsonWriteException extends RuntimeException {
    public JsonWriteException(String message) {
        super(message);
    }

    public JsonWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonWriteException(Throwable cause) {
        super(cause);
    }
}
