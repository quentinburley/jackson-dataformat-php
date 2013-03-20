# Overview

This project contains [Jackson](http://http://wiki.fasterxml.com/JacksonHome)
extension component for reading and writing
[PHP](http://http://php.net/manual/en/function.serialize.php) serialized data.

Project is licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).

# Notes

## PHP Arrays

PHP arrays are both arrays and hashtables which makes it difficult to
go from a PHP array directly to a Java array.  Instead a HashMap with
numeric keys will be returned. 

## PHP Objects

PHP serializes it's objects with names.  These names are discarded and the
objects are treated as maps to be consistent with JSON type behavior.

## Maven dependency

To use this extension on Maven-based projects, use following dependency:

```xml
<dependency>
  <groupId>com.photobucket</groupId>
  <artifactId>jackson-dataformat-php</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

Until this project is ready for release you'll have to checkout the source and
build it yourself first.  This can be done using all the standard maven
commands.

# Usage

Just construct a standard Jackson `ObjectMapper` with `com.photobucket.jackson.dataformat.php.PhpFactory`, like so:

```java
ObjectMapper mapper = new ObjectMapper(new PhpFactory());
User user = mapper.readValue(phpSource, User.class);
```

#Contributors

* Joshua Hollander
