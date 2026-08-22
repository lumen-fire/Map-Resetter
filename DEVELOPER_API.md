# Developer API
The plugin comes with an API you can hook into from bukkit plugins to create, reset, get map saves and more.
## Getting Started
How to add the API to your project. Add or combine the code into your build file.
### Kotlin DSL - build.gradle.kts
```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.lumen-fire:Map-Resetter:api-v1.0.0")
}
```

### Groovy - build.gradle
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.lumen-fire:Map-Resetter:api-v1.0.0'
}
```

### Maven - pom.xml
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
	
<dependency>
    <groupId>com.github.lumen-fire</groupId>
    <artifactId>Map-Resetter</artifactId>
    <version>api-v1.0.0</version>
    <scope>provided</scope>
</dependency>
```

## Usage
See examples of usage in [ExampleAPIUsage.java](example-api-usage/src/main/java/me/lumen/exampleAPI/ExampleAPIUsage.java) and 
read the javadocs [here](https://javadoc.jitpack.io/com/github/lumen-fire/Map-Resetter/api-v1.0.0/javadoc/me/lumen/mapResetterAPI/package-summary.html).

