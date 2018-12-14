package net.hamnaberg.json.io;

import net.hamnaberg.json.Json;
import net.hamnaberg.json.codec.DecodeJson;
import net.hamnaberg.json.codec.DecodeResult;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.Callable;

public abstract class JsonParser {

    public final Json.JValue parseUnsafe(ReadableByteChannel channel) {
        return parseUnsafe(Channels.newInputStream(channel));
    }

    public final Json.JValue parseUnsafe(InputStream is) {
        return parseUnsafe(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)));
    }

    public final Json.JValue parseUnsafe(byte[] bytes) {
        return parseUnsafe(new ByteArrayInputStream(bytes));
    }

    public final Json.JValue parseUnsafe(String string) {
        return parseUnsafe(new StringReader(string));
    }

    public final Json.JValue parseUnsafe(Reader reader) {
        return parseImpl(reader);
    }

    public final Optional<Json.JValue> parseOpt(ReadableByteChannel channel) {
        return parseOpt(Channels.newInputStream(channel));
    }

    public final Optional<Json.JValue> parseOpt(byte[] bytes) {
        return parseOpt(new ByteArrayInputStream(bytes));
    }

    public final Optional<Json.JValue> parseOpt(InputStream is) {
        return parseOpt(new InputStreamReader(is, StandardCharsets.UTF_8));
    }

    public final Optional<Json.JValue> parseOpt(String string) {
        return parseOpt(new StringReader(string));
    }

    public final Optional<Json.JValue> parseOpt(Reader reader) {
        return DecodeResult.fromCallable(() -> parseImpl(reader)).toOption();
    }

    public final <A> DecodeResult<A> decode(ReadableByteChannel is, DecodeJson<A> decoder) {
        return decode(() -> parseUnsafe(is), decoder);
    }

    public final <A> DecodeResult<A> decode(byte[] bytes, DecodeJson<A> decoder) {
        return decode(() -> parseUnsafe(bytes), decoder);
    }

    public final <A> DecodeResult<A> decode(InputStream is, DecodeJson<A> decoder) {
        return decode(() -> parseUnsafe(is), decoder);
    }

    public final <A> DecodeResult<A> decode(String string, DecodeJson<A> decoder) {
        return decode(() -> parseUnsafe(string), decoder);
    }

    public final <A> DecodeResult<A> decode(Reader reader, DecodeJson<A> decoder) {
        return decode(() -> parseUnsafe(reader), decoder);
    }

    public final <A> DecodeResult<A> decode(Callable<Json.JValue> parsed, DecodeJson<A> decoder) {
        return DecodeResult.fromCallable(parsed).flatMap(decoder::fromJson);
    }

    protected abstract Json.JValue parseImpl(Reader reader);
}
