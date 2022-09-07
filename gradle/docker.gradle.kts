import com.bmuschko.gradle.docker.tasks.image.*

apply(plugin = "com.bmuschko.docker-remote-api")
apply(plugin = "com.bmuschko.docker-spring-boot-application")

buildscript {
    val dockerPluginVersion: String by extra
    repositories {
        gradlePluginPortal()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.bmuschko:gradle-docker-plugin:${dockerPluginVersion}")
    }
}
