# Contributing
The project uses java 21 throughout.
## Running the plugin on a server
Runs a paper 26.2 server at ``localhost`` using the default port for minecraft. 
The servers directory will be at ``plugin/run/`` and you have to agree to the minecraft eula the first time you run it.
```shell
 ./gradlew :plugin:runServer
```
## Building the plugin jar
The plugin jar will be located at ``plugin/build/libs/plugin-1.0.0.jar``  
Do not use ``plugin/build/libs/plugin-1.0.0.jar``
```shell
 ./gradlew :plugin:build
```

## Building the API
If you need to build the api fo some reason, here is how.
The javadocs will be at ``api/build/docs/``, and the compiled, sources and javadoc jars at ``api/build/libs``
```shell
 ./gradlew :api:build
```

## Building the API example usage plugin
```shell
 ./gradlew :example-api-usage:build
```

