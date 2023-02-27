# Build commands

To build application:  
```
gradlew clean build 
```

spring boot application run(with utf8 encoding and local properties file):  
```
gradlew -Dfile.encoding=UTF-8 bootRun --args='--spring.config.location=local/resources/application.properties' 
```

# Request commands
````
curl "http://localhost:8090/get-version"
curl "http://localhost:8090/test-endpoint"
curl "http://localhost:8090/walk-for-copies"
curl "http://localhost:8090/get-all-files"
curl "http://localhost:8090/get-files-with-copies"
````
