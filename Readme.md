# Build commands

To build application:  
```
gradlew clean build 
```

spring boot application run(with utf8 encoding and local properties file):  
```
gradlew -Dfile.encoding=UTF-8 bootRun --args='--spring.config.location=local/resources/application.properties' 
```