# Immutable Json library for java

 This project attempts to build and AST ( Abstract Syntactic Tree )
 for Json and provide a useful way to work with that tree.

 If you want to program in functional style in Java, you want
 your data types to be immutable and efficient.

 Unfortunately there are no default immutable collections in Java.
 Since this is the case we need to copy the collections every time we do transformations.

 This will improve when [JEP 269](http://openjdk.java.net/jeps/269) is part of the JDK.

## Status

 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.hamnaberg.json/immutable-json/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.hamnaberg.json/immutable-json)
 [![Build Status](https://travis-ci.org/hamnis/immutable-json.png)](https://travis-ci.org/hamnis/immutable-json)
 [![Gitter Chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/hamnis/immutable-json)


## Where can we find this

 Using maven, you download it from Maven Central using these coordinates:

 ```xml
 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>json-ast</artifactId>
   <version>2.0.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>json-pointer</artifactId>
   <version>2.0.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>json-jackson</artifactId>
   <version>2.0.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>json-javax</artifactId>
   <version>2.0.0</version>
 </dependency>
 ```

 Snapshots can be found from [Sonatype](https://oss.sonatype.org/content/repositories/snapshots/):


 ```xml
 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>json-ast</artifactId>
   <version>3.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>json-pointer</artifactId>
   <version>3.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>json-jackson</artifactId>
   <version>3.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>json-javax</artifactId>
   <version>3.0-SNAPSHOT</version>
 </dependency>
  ```

## License

 We are using the Apache License 2.0
