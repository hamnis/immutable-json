#!/usr/bin/env scala

//import java.io.File

def applyTemplate(arity: Int) = {
  val arities = (1 to arity)
  val types = arities.map(i => "A"+i).mkString(", ")
  val params = arities.map(i => s"A$i _$i").mkString(", ")
  val fieldsNames = arities.map(i => s"_$i").mkString(", ")

  s"""
     |public static <$types> Tuple$arity<$types> of($params) {
     |    return new Tuple$arity<>($fieldsNames);
     |}
    """.stripMargin
}

//println(applyTemplate(9))


(1 to 27).map(i => applyTemplate(i)).foreach{
  println
}
