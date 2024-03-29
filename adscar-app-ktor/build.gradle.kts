import org.gradle.api.file.DuplicatesStrategy.EXCLUDE
import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project
val kotlinVersion: String by project

// ex: Converts to "io.ktor:ktor-ktor-server-netty:2.0.1" with only ktor("netty")
fun ktor(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    id("application")
    kotlin("plugin.serialization")
    kotlin("jvm")
    id("io.ktor.plugin")
}

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

application {
    mainClass.set("AppKt")
}

ktor {
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(io.ktor.plugin.features.JreVersion.JRE_17)
    }
}

jib {
    container.mainClass = "io.ktor.server.cio.EngineMain"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion"
    implementation(ktor("netty")) // "io.ktor:ktor-ktor-server-netty:$ktorVersion"

    // jackson
    implementation(ktor("jackson", "serialization")) // io.ktor:ktor-serialization-jackson
    implementation(ktor("content-negotiation")) // io.ktor:ktor-server-content-negotiation
    implementation(ktor("kotlinx-json", "serialization")) // io.ktor:ktor-serialization-kotlinx-json

    implementation(ktor("locations"))
    implementation(ktor("caching-headers"))
    implementation(ktor("call-logging"))
    implementation(ktor("auto-head-response"))
    implementation(ktor("cors")) // "io.ktor:ktor-cors:$ktorVersion"
    implementation(ktor("default-headers"))
    implementation(ktor("cio")) // "io.ktor:ktor-server-cio:$ktorVersion"

    implementation(ktor("websockets")) // "io.ktor:ktor-websockets:$ktorVersion"
    implementation(ktor("auth")) // "io.ktor:ktor-auth:$ktorVersion"
    implementation(ktor("auth-jwt")) // "io.ktor:ktor-auth-jwt:$ktorVersion"

    implementation(ktor("config-yaml"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation(project(":adsCar-api-v1-jackson"))
    implementation(project(":adsCar-mappers-v1"))
    implementation(project(":adsCar-common"))
    implementation(project(":adsCar-business-logic"))
    implementation(project(":adsCar-app-common"))

    implementation(project(":adsCar-stubs"))

    implementation(project(":adsCar-logging"))
    implementation(project(":adsCar-mappers-log"))
    implementation(project(":adsCar-log-models"))

    implementation(project(":app-repo-in-memory"))
    implementation(project(":app-repo-cassandra"))
    implementation(project(":app-repo-stubs"))

    implementation("com.sndyuk:logback-more-appenders:1.8.8")
    implementation("org.fluentd:fluent-logger:0.3.4")

    implementation("org.testcontainers:cassandra:1.19.3")

    testImplementation(kotlin("test-junit"))
    testImplementation(ktor("test-host")) // "io.ktor:ktor-server-test-host:$ktorVersion"
    testImplementation(ktor("content-negotiation", prefix = "client-"))
    testImplementation(ktor("websockets", prefix = "client-"))

    testImplementation(project(":app-repo-tests"))
}

tasks.withType<Copy> {
    duplicatesStrategy = EXCLUDE
}
