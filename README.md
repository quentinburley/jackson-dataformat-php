# Overview

This project contains [Jackson](http://http://wiki.fasterxml.com/JacksonHome)
extension component for reading and writing
[PHP](http://http://php.net/manual/en/function.serialize.php) serialized data.

Project is licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).

# Features

* Serialization/Deserialization of all PHP primitives
* Serialization/Deserialization of PHP arrays
* Serialization/Deserialization of PHP objects

## Notes and Issues

The PHP serialization format is very PHP-ish and reasonably so.  As a result
the translation between Java and PHP is not entirely symmetrical. Below are
some notes and issues with the translation.

### PHP Arrays

PHP arrays are both arrays and hashtables which makes it difficult to
go from a PHP array directly to a Java array.  Instead a HashMap with
numeric keys will be returned. Hopefully a way will be found around this issue.

### PHP Objects

PHP serializes it's objects with names.  Since these names are meaningless to
Java they are discarded and the objects are treated as maps to be consistent
with JSON behavior.  This leads to object serializtion being asymmetric.

# Instalation

## Maven dependency

Until this project is ready for release you'll have to checkout the source and
build it yourself first.  This can be done using all the standard maven
commands.

To use this extension on Maven-based projects, use following dependency:

```xml
<dependency>
  <groupId>com.photobucket</groupId>
  <artifactId>jackson-dataformat-php</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

# Usage

Just construct a standard Jackson `ObjectMapper` with `com.photobucket.jackson.dataformat.php.PhpFactory`, like so:

```java
ObjectMapper mapper = new ObjectMapper(new PhpFactory());
User user = mapper.readValue(phpSource, User.class);
```

#Contributors

* Joshua Hollander
* Stephan Wienczny
* Quentin Burley
