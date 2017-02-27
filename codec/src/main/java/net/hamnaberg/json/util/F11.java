
package net.hamnaberg.json.util;

import java.util.function.Function;

@FunctionalInterface
public interface F11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, B> {
   B apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6, A7 a7, A8 a8, A9 a9, A10 a10, A11 a11);

   default Function<Tuple11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11>, B> tupled() {
      return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11);
   }
}
    