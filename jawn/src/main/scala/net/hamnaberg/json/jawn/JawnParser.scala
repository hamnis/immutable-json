package net.hamnaberg.json.jawn

import java.io.{BufferedReader, Reader}
import java.util.stream.Collectors

import io.vavr.control.Try
import jawn.{FContext, Facade, Parser}
import net.hamnaberg.json.Json
import net.hamnaberg.json.io.JsonParser

class JawnParser extends JsonParser {
  private implicit val facade = IJFacade

  override protected def parseImpl(reader: Reader): Try[Json.JValue] = {
    val str = reader.asInstanceOf[BufferedReader].lines().collect(Collectors.joining("\n"))
    Parser.parseFromString(str).fold(Try.failure(_), Try.success(_))
  }
}

private[jawn] object IJFacade extends Facade[Json.JValue] {

  import collection.JavaConverters._

  final override def singleContext(): FContext[Json.JValue] = new FContext[Json.JValue] {
    var value: Json.JValue = _

    override def add(s: CharSequence): Unit = add(jstring(s))

    override def add(v: Json.JValue): Unit = value = v

    override def isObj: Boolean = false

    override def finish: Json.JValue = value
  }

  final override def arrayContext(): FContext[Json.JValue] = new FContext[Json.JValue] {
    val list = collection.mutable.ArrayBuffer[Json.JValue]()

    override def add(s: CharSequence): Unit = add(jstring(s))

    override def add(v: Json.JValue): Unit = list += v

    override def isObj: Boolean = false

    override def finish: Json.JValue = Json.jArray(list.asJava)

  }

  final override def objectContext(): FContext[Json.JValue] = new FContext[Json.JValue] {
    var key: String = _
    val map = collection.mutable.HashMap[String, Json.JValue]()

    override def add(s: CharSequence): Unit = {
      if (key == null) key = s.toString
      else {
        map(key) = jstring(s)
        key = null
      }
    }

    override def add(v: Json.JValue): Unit = {
        map(key) = v
        key = null
    }

    override def isObj: Boolean = true

    override def finish: Json.JValue = Json.jObject(map.asJava)
  }

  final override def jnull(): Json.JValue = Json.jNull()

  final override def jfalse(): Json.JValue = Json.jBoolean(false)

  final override def jtrue(): Json.JValue = Json.jBoolean(true)


  final override def jnum(s: CharSequence, decIndex: Int, expIndex: Int) = Json.jNumber(BigDecimal(s.toString))

  final override def jstring(s: CharSequence): Json.JValue = Json.jString(s.toString)
}
