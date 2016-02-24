#!/usr/bin/env scala

val arities = ('A' to 'W').toList

val zipped = arities.zipWithIndex

for((_, index) <- zipped) {
  val arity = index
  if (arity > 0) {
    val currentArity = arities.take(index)
    val zippedCurrent = currentArity.zipWithIndex.map{case (a, i) => a -> (i+1)}
    val genericsArgs = currentArity.mkString(", ")

    val typeArgs = zippedCurrent.map{case (a,i) => s"TypedField<$a> f${i}"}.mkString(", ")
    val fromJsonOpt = zippedCurrent.map{case (a, i) =>
      s"Optional<$a> o${Character.toLowerCase(a)} = object.getAs(f${i}.name, f${i}.decoder::fromJson);"
    }.mkString("\n      ")

    val fromJson = {
      val fromJsonflatMap = zippedCurrent.map{case (a, i) =>
        val lower = Character.toLowerCase(a)
        s"o${lower}.flatMap(${lower} -> "
      }
      val finalFromJsonMap = currentArity.map{ a => Character.toLowerCase(a) }.mkString(s"Optional.of(func.apply(", ", ", "))")
      fromJsonflatMap.mkString("", "",  finalFromJsonMap + (")" * arity) + ";")
    }

    val template =
      s"""
         |public static <TT, ${genericsArgs}> Extractor<TT> extract$arity($typeArgs, javaslang.Function$arity<$genericsArgs, TT> func) {
         |    return (object) -> {
         |      $fromJsonOpt
         |      return $fromJson
         |    };
         |}
      """.stripMargin
    println(template)
  }
}
