#!/usr/bin/env scala

def template(arity: Int)= {
  val arities = (1 to arity)
  val types = arities.map(i => s"A$i").mkString(", ")
  val params = arities.map(i => s"FieldEncoder<A$i> e$i").mkString(", ")
  val toJson = arities.map(i => s"                Json.tuple(e$i.name, e$i.toJson(tuple._$i))").mkString(",\n")


  val values = arities.map(i => s"e$i").mkString(", ")

  s"""|public static <$types> EncodeJson<Tuple$arity<$types>> encode($params) {
      |    return tuple -> Json.jObject(
      |$toJson
      |    );
      |}
      |
      |public static <$types, TT> EncodeJson<TT> encode($params, Function<TT, Tuple$arity<$types>> f) {
      |    return type -> encode($values).toJson(f.apply(type));
      |}
     |""".stripMargin
}


(2 to 27).foreach{ i =>
  println(template(i))
}
