#!/usr/bin/env scala

import java.io.File

def applyTemplate(arity: Int) = {
  val types = (1 to arity).map(i => "A"+i).mkString(", ")
  val input = (1 to arity).map(i => s"A$i a$i").mkString(", ")
  val tupledValues = (1 to arity).map(i => s"t._$i").mkString(", ")

  s"""
     |package net.hamnaberg.json.util;
     |
     |import java.util.function.Function;
     |
     |@FunctionalInterface
     |public interface F$arity<$types, B> {
     |   B apply($input);
     |
     |   default Function<Tuple$arity<$types>, B> tupled() {
     |      return t -> apply($tupledValues);
     |   }
     |}
    """.stripMargin
}


val target = new File("codec/src/main/java/net/hamnaberg/json/util")

if (!target.exists() && !target.mkdirs()) {
  sys.error("WHAT!?!")
}

(9 to 27).map(i => new File(target, s"F$i.java") -> applyTemplate(i)).foreach{
  case (f, s) => {
    java.nio.file.Files.write(f.toPath, s.getBytes(java.nio.charset.StandardCharsets.UTF_8))
  }
}

//println(applyTemplate(9))
