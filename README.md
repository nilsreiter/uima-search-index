[![Build Status](https://travis-ci.org/nilsreiter/uima-search-index.svg?branch=master)](https://travis-ci.org/nilsreiter/uima-search-index)

# uima-util
Utility components for Apache UIMA


## Availability

uima-util is licensed under the Apache License 2.0 and is available via Maven Central.
If you use Maven for your build environment, then you can
add uima-util as a dependency to your pom.xml file with the following:

```
<dependency>
  <groupId>de.unistuttgart.ims</groupId>
  <artifactId>uima-search-index</artifactId>
  <version>0.1.0</version>
</dependency>
```


## Build
- `mvn -DperformRelease=true deploy` to deploy to maven central.
- `mvn clean javadoc:javadoc scm-publish:publish-scm` publish javadoc to github
