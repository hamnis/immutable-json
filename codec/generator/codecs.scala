#!/usr/bin/env scala

val arities = ('A' to 'W').toList

val zipped = arities.zipWithIndex

for((_, index) <- zipped) {
  val arity = index
  if (arity > 0) {
    val currentArity = arities.take(index)
    val zippedCurrent = currentArity.zipWithIndex.map{case (a, i) => a -> (i+1)}
    val stringArgs = (1 to arity).map(_ => "String").mkString(",")
    val genericsArgs = currentArity.mkString(", ")
    val namedArgs = (1 to arity).map(i => "n" + i).mkString(",")
    val typeArgs = zippedCurrent.map{case (a,i) => s"JsonCodec<$a> c${i}"}.mkString(", ")
    val fromJsonOpt = zippedCurrent.map{case (a, i) =>
      s"Optional<$a> o${Character.toLowerCase(a)} = object.getAs(n${i}, c${i}::fromJson);"
    }.mkString("\n            ")

    val toJson = {
      val flatMapped = zippedCurrent.map{case (_, i) =>
        s"c$i.toJson(tuple._$i).flatMap(j$i -> "
      }
      val finaltoJsonMap = zippedCurrent.map{ case (_, i) => s"Json.entry(n$i, j$i)" }.mkString("Optional.of(Json.jObject(", ",","))")
      flatMapped.mkString("", "",  finaltoJsonMap + (")" * arity) + ";")
    }

    val fromJson = {
      val fromJsonflatMap = zippedCurrent.map{case (a, i) =>
        val lower = Character.toLowerCase(a)
        s"o${lower}.flatMap(${lower} -> "
      }
      val finalFromJsonMap = currentArity.map{ a => Character.toLowerCase(a) }.mkString(s"Optional.of(iso.reverseGet(new javaslang.Tuple$arity<>(", ",", ")))")
      fromJsonflatMap.mkString("", "",  finalFromJsonMap + (")" * arity) + ";")
    }


    val template =
      s"""
        |public static <TT, ${genericsArgs}> javaslang.Function$arity<${stringArgs}, JsonCodec<TT>> codec$arity(Iso<TT, javaslang.Tuple$arity<$genericsArgs>> iso, $typeArgs) {
        |    return ($namedArgs) -> new JsonCodec<TT>() {
        |        @Override
        |        public Optional<Json.JValue> toJson(TT value) {
        |            javaslang.Tuple$arity<$genericsArgs> tuple = iso.get(value);
        |            return $toJson
        |        }
        |
        |        @Override
        |        public Optional<TT> fromJson(Json.JValue value) {
        |            Json.JObject object = value.asJsonObjectOrEmpty();
        |            $fromJsonOpt
        |            return $fromJson
        |        }
        |    };
        |}
        |
      """.stripMargin

    println(template)
  }
}
