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
  val toJson = arities.map(i => s"                c$i.toFieldEncoder()").mkString(",\n")
  val fromJson = arities.map(i => s"                c$i.toFieldDecoder()").mkString("", ",\n", ",")

  val toStringMap = arities.map(i => s"                map.put(c$i.name, c$i.codec.toString())").mkString("", ";\n", ";")

  s"""
     |public static <TT, $types> JsonCodec<TT> codec(Iso<TT, Tuple$arity<$types>> iso, $codecParams) {
     |    return new JsonCodec<TT>() {
     |
     |        @Override
     |        public Json.JValue toJson(TT value) {
     |            return Encoders.encode(
     |$toJson
     |            ).toJson(iso.get(value));
     |        }
     |
     |        @Override
     |        public DecodeResult<TT> fromJson(Json.JValue value) {
     |            return Decoders.decode(
     |$fromJson
     |                Tuple$arity::new
     |            ).fromJson(value).map(iso::reverseGet);
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

/*
(2 to 27).foreach{ i =>
  println(ofTemplate(i))
}*/

(2 to 27).foreach{ i =>
  println(codecTemplate(i))
}

