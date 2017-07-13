#!/usr/bin/env scala

def template(arity: Int)= {
  val functionName = if (arity < 9) "Function" else "F"
  val arities = (1 to arity)
  val types = arities.map(i => s"A$i").mkString(", ")
  val params = arities.map(i => s"FieldDecoder<A$i> fd$i").mkString(", ")
  val apply = arities.map(i => s"d$i.flatMap(v$i -> ").mkString("")
  val extractions = arities.map(i => s"      DecodeResult<A$i> d$i = DecodeResult.decode(object, fd$i);").mkString("\n")
  val endParams = arities.map(_ => ")").mkString
  val values = arities.map(i => s"v$i").mkString(", ")

  s"""|public static <TT, $types> DecodeJson<TT> decode($params, $functionName$arity<$types, TT> func) {
     |  return (value) -> {
     |      Json.JObject object = value.asJsonObjectOrEmpty();
     |$extractions
     |      return $apply DecodeResult.ok(func.apply($values)$endParams);
     |  };
     |}
     |""".stripMargin
}


(2 to 27).foreach{ i =>
  println(template(i))
}
