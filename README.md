# Immutable Json library for java

 This project attempts to build and AST ( Abstract Syntactic Tree )
 for Json and provide a useful way to work with that tree.

 If you want to program in functional style in Java, you want
 your data types to be immutable and efficient.

 Unfortunately there are no default immutable collections in Java, so we try to use javaslang where appropriate.


## Status

 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.hamnaberg.json/immutable-json/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.hamnaberg.json/immutable-json)
 [![Build Status](https://travis-ci.org/hamnis/immutable-json.png)](https://travis-ci.org/hamnis/immutable-json)
 [![Gitter Chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/hamnis/immutable-json)

## Usage

A short example showing encoding/decoding of a few different types.
Note the `List` type is from `javaslang.collection` to have an immutable List type.


```java
    static class Event {
        public final UUID ID;
        public final List<String> tags;
        public final String message;

        public Event(UUID ID, List<String> tags, String message) {
            this.ID = ID;
            this.tags = tags;
            this.message = message;
        }
    }

    @Test
    public void testEvent() throws Exception {
        String expected = "{"+
                      "\"id\":\"1e2f28ff-54b5-4ad4-9edb-36712dc52202\","+
                      "\"tags\":[\"travel\",\"code\"],"+
                      "\"message\":\"This is a test\""+
                   "}";

        // create a decoder for our Event
        DecodeJson<Event> decode = Decoders.decode(
                FieldDecoder.TString("id").tryNarrow(UUID::fromString),
                FieldDecoder.TJArray("tags")
                        .mapToList(jValue -> jValue.asString().getOrElse(""))
                        .withDefaultValue(List.empty()),
                FieldDecoder.TString("message"),
                Event::new
        );

        // create an encoder that will encode our Event into json
        EncodeJson<Event> encode = Encoders.encode(
                FieldEncoder.typedFieldOf("id", Encoders.EString.contramap(UUID::toString)),
                FieldEncoder.EList("tags", Encoders.EString),
                FieldEncoder.EString("message")
        ).contramap(event -> Tuple.of(event.ID, event.tags, event.message));

        // a codec can both encode and decode a value
        JsonCodec<Event> codec = JsonCodec.lift(decode, encode);

        // parse raw json with help of jackson parser
        Json.JValue jValue = new JacksonStreamingParser().parse(expected);

        String json = codec
                .toJson(codec.fromJson(jValue).unsafeGet())
                .nospaces();

        assertThat(json).isEqualTo(expected);
    }
```


## Where can we find this

 Using maven, you download it from Maven Central using these coordinates:

 ```xml
 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-ast</artifactId>
   <version>5.2.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-pointer</artifactId>
   <version>5.2.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-patch</artifactId>
   <version>5.2.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-jackson</artifactId>
   <version>5.2.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-native</artifactId>
   <version>5.2.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-javax</artifactId>
   <version>5.2.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-codec</artifactId>
   <version>5.2.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-reflection-codec</artifactId>
   <version>5.2.0</version>
 </dependency>
 ```

 Snapshots can be found from [Sonatype](https://oss.sonatype.org/content/repositories/snapshots/):


 ```xml
 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-ast</artifactId>
   <version>5.3.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-pointer</artifactId>
   <version>5.3.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-jackson</artifactId>
   <version>5.3.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-native</artifactId>
   <version>5.3.0-SNAPSHOT</version>
  </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-javax</artifactId>
   <version>5.3.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-codec</artifactId>
   <version>5.3.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-reflection-codec</artifactId>
   <version>5.3.0-SNAPSHOT</version>
 </dependency>
  ```

## License

 We are using the Apache License 2.0


## Contributing 
All contributions are welcome! Documentation is sorely lacking and really needs some love.
If you find a missing combinator, please open a PR or issue so we can discuss it.
Any bugs or missing features are also welcome. 