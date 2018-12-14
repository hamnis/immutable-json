# Immutable Json library for java

 This project attempts to build and AST ( Abstract Syntactic Tree )
 for Json and provide a useful way to work with that tree.
 
 ## Note
Starting with version 7, we require JDK11.
Vavr has been dropped as a dependency.


## Status

 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.hamnaberg.json/immutable-json/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.hamnaberg.json/immutable-json)
 [![Build Status](https://travis-ci.org/hamnis/immutable-json.png)](https://travis-ci.org/hamnis/immutable-json)
 [![Gitter Chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/hamnis/immutable-json)


# Community

## Adopters

Are you using immutable-json? Please consider opening a pull request to list your organization here:

* Your Organization here

## Related projects

* [Siren.java](https://github.com/arktekk/siren.java)
* [Collection JSON (Java)](https://github.com/hamnis/json-collection)


# Usage

A short example showing encoding/decoding of a few different types.
Also note that the `Tuples` type is from `net.hamnaberg.json.util` to have Tuple constructors up to `Tuple27`


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
                Decoders.DUUID.fieldDecoder("id"),
                FieldDecoder.TList("tags", Decoders.DString).withDefaultValue(Collections.emptyList()),
                FieldDecoder.TString("message"),
                Event::new
        );

        // create an encoder that will encode our Event into json
        EncodeJson<Event> encode = Encoders.encode(
                Encoders.EUUID.fieldEncoder("id"),
                FieldEncoder.EList("tags", Encoders.EString),
                FieldEncoder.EString("message")
        ).contramap(event -> Tuples.of(event.ID, event.tags, event.message));

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

# Where can we find this

 Using maven, you download it from Maven Central using these coordinates:

 ```xml
 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-ast</artifactId>
   <version>7.0.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-pointer</artifactId>
   <version>7.0.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-patch</artifactId>
   <version>7.0.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-jackson</artifactId>
   <version>7.0.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-native</artifactId>
   <version>7.0.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-javax</artifactId>
   <version>7.0.0</version>
 </dependency>

<dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-gson</artifactId>
   <version>7.0.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-codec</artifactId>
   <version>7.0.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-reflection-codec</artifactId>
   <version>7.0.0</version>
 </dependency>
 ```

 Snapshots can be found from [Sonatype](https://oss.sonatype.org/content/repositories/snapshots/):


 ```xml
 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-ast</artifactId>
   <version>7.1.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-pointer</artifactId>
   <version>7.1.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-jackson</artifactId>
   <version>7.1.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-native</artifactId>
   <version>7.1.0-SNAPSHOT</version>
  </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-javax</artifactId>
   <version>7.1.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-codec</artifactId>
   <version>7.1.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-reflection-codec</artifactId>
   <version>7.1.0-SNAPSHOT</version>
 </dependency>
  ```

## License

 We are using the Apache License 2.0


## Contributing 
All contributions are welcome! Documentation is sorely lacking and really needs some love.
If you find a missing combinator, please open a PR or issue so we can discuss it.
Any bugs or missing features are also welcome. 
