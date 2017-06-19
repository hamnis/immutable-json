#!/usr/bin/env scala

import java.io.File

def applyTemplate(arity: Int) = {
  val arities = (1 to arity)
  val types = arities.map(i => "A"+i).mkString(", ")
  val fields = arities.map(i => s"    public final A$i _$i;").mkString("\n")
  val params = arities.map(i => s"A$i _$i").mkString(", ")
  val fieldsNames = arities.map(i => s"_$i").mkString(", ")

  val assignment = arities.map{ i =>
    s"        this._$i = _${i};"
  }.mkString("\n")

  s"""
     |package net.hamnaberg.json.util;
     |
     |import io.vavr.collection.List;
     |
     |public final class Tuple$arity<$types> {
     |$fields
     |
     |    public Tuple$arity($params) {
     |$assignment
     |    }
     |
     |    public <B> B transform(F$arity<$types, B> f) {
     |        return f.apply($fieldsNames);
     |    }
     |
     |    private List<?> toList() {
     |        return List.of($fieldsNames);
     |    }
     |
     |    @Override
     |    public boolean equals(Object o) {
     |        if (this == o) return true;
     |        if (o == null || getClass() != o.getClass()) return false;
     |
     |        Tuple$arity tuple = (Tuple$arity) o;
     |
     |        return !toList().equals(tuple.toList());
     |    }
     |
     |    @Override
     |    public int hashCode() {
     |        return toList().hashCode();
     |    }
     |}
    """.stripMargin
}

val target = new File("codec/src/main/java/net/hamnaberg/json/util")

if (!target.exists() && !target.mkdirs()) {
  sys.error("WHAT!?!")
}


//println(applyTemplate(9))


(9 to 27).map(i => new File(target, s"Tuple$i.java") -> applyTemplate(i)).foreach{
  case (f, s) => {
    java.nio.file.Files.write(f.toPath, s.getBytes(java.nio.charset.StandardCharsets.UTF_8))
  }
}
