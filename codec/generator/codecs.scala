#!/usr/bin/env scala

def ofTemplate(arity: Int) = {
  val arities = (1 to arity)
  val types = arities.map(i => s"A$i").mkString(", ")
  val params = arities.map(i => s"NamedJsonCodec<A$i> c$i").mkString(", ")
  val values = arities.map(i => s"c$i").mkString(", ")


  s"""
    |public static <$types> JsonCodec<Tuple$arity<$types>> of($params) {
    |    return codec(Iso.identity(), $values);
    |}
  """.stripMargin
}

def codecTemplate(arity: Int) = {

  val arities = (1 to arity)
  val types = arities.map(i => s"A$i").mkString(", ")
  val codecParams = arities.map(i => s"NamedJsonCodec<A$i> c$i").mkString(", ")
  val toJson = arities.map(i => s"                Json.tuple(c$i.name, c$i.toJson(tuple._$i))").mkString(",\n")

  val fromValues = arities.map(i => s"            DecodeResult<A$i> d$i = DecodeResult.decode(object, c$i.name, c$i);").mkString("\n")

  val decode = arities.map(i => s"d$i.flatMap(v$i -> ").mkString("")
  val decodeEndParams = arities.map(_ => ")").mkString
  val decodeValues = arities.map(i => s"v$i").mkString(", ")

  val toStringMap = arities.map(i => s"                map.put(c$i.name, c$i.toString())").mkString("", ";\n", ";")

  s"""
     |public static <TT, $types> JsonCodec<TT> codec(Iso<TT, Tuple$arity<$types>> iso, $codecParams) {
     |    return new JsonCodec<TT>() {
     |
     |        @Override
     |        public Json.JValue toJson(TT value) {
     |            Tuple$arity<$types> tuple = iso.get(value);
     |            return Json.jObject(
     |$toJson
     |            );
     |        }
     |
     |        @Override
     |        public DecodeResult<TT> fromJson(Json.JValue value) {
     |            Json.JObject object = value.asJsonObjectOrEmpty();
     |$fromValues
     |            return $decode DecodeResult.ok(iso.reverseGet(new Tuple$arity<>($decodeValues))$decodeEndParams);
     |        }
     |
     |        @Override
     |        public String toString() {
     |            Map<String, String> map = new HashMap<>();
     |$toStringMap
     |            return "codec" + map.toString();
     |        }
     |    };
     |}
  """.stripMargin

}


(9 to 27).foreach{ i =>
  println(ofTemplate(i))
}

/*(9 to 27).foreach{ i =>
  println(codecTemplate(i))
}*/

