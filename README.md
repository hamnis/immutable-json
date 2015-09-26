# Immutable Json library for java

 This project attempts to build and AST ( Abstract Syntactic Tree )
 for Json and provide a useful way to work with that tree.

 If you want to program in functional style in Java, you want
 your data types to be immutable and efficient.

 Unfortunately there are no default immutable collections
 in Java, which is why we are depending on [Javaslang](http://javaslang.com).

 I would really like to avoid having this dependency, so when [JEP 269](http://openjdk.java.net/jeps/269)
 is part of the JDK, then we should consider dropping it.

## Status

 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.hamnaberg.json/immutable-json/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.hamnaberg.json/immutable-json)
 [![Build Status](https://travis-ci.org/hamnis/immutable-json.png)](https://travis-ci.org/hamnis/immutable-json)
 [![Gitter Chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/hamnis/immutable-json)


## Where can we find this

 Using maven, you download it from Maven Central using these coordinates:

 ```xml
 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json</artifactId>
   <version>CURRENTLY_UNRELASED</version>
 </dependency>
 ```

 Snapshots can be found from [Sonatype](https://oss.sonatype.org/content/repositories/snapshots/):


 ```xml
    <dependency>
      <groupId>net.hamnaberg.json</groupId>
      <artifactId>immutable-json</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
  ```

## License

 We are using the Apache License 2.0
