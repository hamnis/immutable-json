/*
 * Copyright 2018 Erlend Hamnaberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package net.hamnaberg.json.codec;

import java.util.concurrent.Callable;
import java.util.function.Function;

public abstract class Callables {
    private Callables() {
    }

    public static <A, B> Callable<B> map(Callable<A> callable, Function<A, B> f) {
        return () -> f.apply(callable.call());
    }

    public static <A, B> Callable<B> flatMap(Callable<A> callable, Function<A, Callable<B>> f) {
        return () -> f.apply(callable.call()).call();
    }

    public static <A> A getOrElse(Callable<A> callable, A orElse) {
        return fold(callable, Function.identity(), ignore -> orElse);
    }

    private static <A, B> B fold(Callable<A> callable, Function<A, B> success, Function<Exception, B> failure) {
        try {
            return success.apply(callable.call());
        } catch (Exception e) {
            return failure.apply(e);
        }
    }
}
