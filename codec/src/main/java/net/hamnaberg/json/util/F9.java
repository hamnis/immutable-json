
package net.hamnaberg.json.util;

import java.util.function.Function;

@FunctionalInterface
public interface F9<A1, A2, A3, A4, A5, A6, A7, A8, A9, B> {
   B apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6, A7 a7, A8 a8, A9 a9);

   default Function<Tuple9<A1, A2, A3, A4, A5, A6, A7, A8, A9>, B> tupled() {
      return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9);
   }
}
    