package net.hamnaberg.json;

import javaslang.control.Option;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class DecodeResult<A> {

    public final <B> DecodeResult<B> map(Function<A, B> f) {
        return fold(okValue -> ok(f.apply(okValue.value)), fail -> fail(fail.message));
    }

    public final void forEach(Consumer<A> f) {
        foldUnit(okValue -> f.accept(okValue.value), ignore -> {});
    }

    public final <B> DecodeResult<B> flatMap(Function<A, DecodeResult<B>> f) {
        return fold(okValue -> f.apply(okValue.value), fail -> fail(fail.message));
    }

    public final A getOrElse(Supplier<A> orElse) {
        return fold(Ok::getValue, ignore -> orElse.get());
    }

    public final A unsafeGet() {
        return fold(Ok::getValue, e -> {
            throw new NoSuchElementException(e.message);
        });
    }

    public final Option<A> toOption() {
        return fold(ok -> Option.of(ok.value), ignore -> Option.none());
    }

    public final Optional<A> toJavaOptional() {
        return toOption().toJavaOptional();
    }

    public abstract <B> B fold(Function<Ok<A>, B> okFunction, Function<Failure, B> failFunction);

    public abstract void foldUnit(Consumer<Ok<A>> okFunction, Consumer<Failure> failFunction);

    public boolean isOk() {
        return fold(a -> true, a -> false);
    }

    public boolean isFailure() {
        return fold(a -> false, a -> true);
    }

    public static <A> DecodeResult<A> ok(A value) {
        return new Ok<>(value);
    }

    public static <A> DecodeResult<A> fromOption(Option<A> value) {
        return value.map(DecodeResult::ok).getOrElse(fail("No value found"));
    }

    @SuppressWarnings("unchecked")
    public static <A> DecodeResult<A> fail(String message) {
        return (DecodeResult<A>) new Failure(message);
    }


    private static DecodeResult<Json.JValue> getValue(Json.JObject object, String name) {
        return object.
                get(name).
                map(DecodeResult::ok).
                getOrElse(DecodeResult.fail(String.format("%s not found in object", name)));
    }

    public static <A> DecodeResult<A> decode(Json.JObject object, String name, DecodeJson<A> decoder) {
        DecodeResult<A> result = getValue(object, name).flatMap(decoder::fromJson);
        if (result.isFailure()) {
            Option<A> defaultValue = decoder.defaultValue();
            if (defaultValue.isDefined()) {
                result = DecodeResult.ok(defaultValue.get());
            }
        }
        return result;
    }

    public final static class Ok<A> extends DecodeResult<A> {
        public final A value;

        public Ok(A value) {
            this.value = value;
        }

        @Override
        public <B> B fold(Function<Ok<A>, B> okFunction, Function<Failure, B> failFunction) {
            return okFunction.apply(this);
        }

        @Override
        public void foldUnit(Consumer<Ok<A>> okFunction, Consumer<Failure> failFunction) {
            okFunction.accept(this);
        }

        public A getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("Ok(value='%s')", value);
        }
    }

    public final static class Failure extends DecodeResult<Object> {
        private final String message;

        public Failure(String message) {
            this.message = message;
        }

        @Override
        public <B> B fold(Function<Ok<Object>, B> okFunction, Function<Failure, B> failFunction) {
            return failFunction.apply(this);
        }

        @Override
        public void foldUnit(Consumer<Ok<Object>> okFunction, Consumer<Failure> failFunction) {
            failFunction.accept(this);
        }

        @Override
        public String toString() {
            return String.format("Failure(message='%s')", message);
        }
    }

}
