#!/usr/bin/env scala

def template(arity: Int)= {

  val arities = (1 to arity)
  val types = arities.map(i => s"A$i").mkString(", ")
  val params = arities.map(i => s"TypedField<A$i> tf$i").mkString(", ")
  val apply = arities.map(i => s"d$i.flatMap(v$i -> ").mkString("")
  val extractions = arities.map(i => s"     DecodeResult<A$i> d$i = DecodeResult.decode(object, tf$i.name, tf$i.decoder);").mkString("\n")
  val endParams = arities.map(_ => ")").mkString
  val values = arities.map(i => s"v$i").mkString(", ")

  s"""|public static <TT, $types> Extractor<TT> extract($params, F$arity<$types, TT> func) {
     |  return (object) -> {
     |$extractions
     |    return $apply DecodeResult.ok(func.apply($values)$endParams);
     |  };
     |}
     |""".stripMargin
}


(9 to 27).foreach{ i =>
  println(template(i))
}
