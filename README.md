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


## Where can we find this

 Using maven, you download it from Maven Central using these coordinates:

 ```xml
 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-ast</artifactId>
   <version>3.6.0</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-pointer</artifactId>
   <version>3.6.1</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-patch</artifactId>
   <version>3.6.1</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-jackson</artifactId>
   <version>3.6.1</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-native</artifactId>
   <version>3.6.1</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-javax</artifactId>
   <version>3.6.1</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-codec</artifactId>
   <version>3.6.1</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-reflection-codec</artifactId>
   <version>3.6.1</version>
 </dependency>
 ```

 Snapshots can be found from [Sonatype](https://oss.sonatype.org/content/repositories/snapshots/):


 ```xml
 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-ast</artifactId>
   <version>4.0.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-pointer</artifactId>
   <version>4.0.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-jackson</artifactId>
   <version>4.0.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-native</artifactId>
   <version>4.0.0-SNAPSHOT</version>
  </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-javax</artifactId>
   <version>4.0.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-codec</artifactId>
   <version>4.0.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>net.hamnaberg.json</groupId>
   <artifactId>immutable-json-reflection-codec</artifactId>
   <version>4.0.0-SNAPSHOT</version>
 </dependency>
  ```

## License

 We are using the Apache License 2.0
