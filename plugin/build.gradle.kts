plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("com.gradleup.shadow") version "9.6.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.100-stable")
    implementation(project(":api"))
}

tasks {
    processResources {
        val props = mapOf("version" to version , "description" to project.description )
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
    //runs a paper server
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("26.2")
    }
    build {
        dependsOn(shadowJar)
    }
}
