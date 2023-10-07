rootProject.name = "ZyablovAdsCar"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openApiVersion: String by settings
    val bmuschkoVersion: String by settings

    val springframeworkBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val pluginSpringVersion: String by settings
    val pluginJpa: String by settings
    val ktorVersion: String by settings
    val pluginShadow: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion apply false
        id("io.kotest.multiplatform") version kotestVersion apply false
        id("org.openapi.generator") version openApiVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("io.ktor.plugin") version ktorVersion apply false
        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false

        id("org.springframework.boot") version springframeworkBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version pluginSpringVersion apply false
        kotlin("plugin.jpa") version pluginJpa apply false

        id("io.ktor.plugin") version ktorVersion apply false

        id("com.github.johnrengelman.shadow") version pluginShadow apply false
    }
}

// include("m1l1-quickstart")
// include("m1l1-hello-world")
// include("m1l3-oop")
// include("m1l4-dsl")
// include("m1l5-coroutines")
// include("m1l6-flows-and-channels")
// include("m1l7-kmp")
// include("m2l2-testing")
include("adsCar-api-v1-jackson")
include("adsCar-common")
include("adsCar-mappers-v1")
include("adscar-app-ktor")
include("adsCar-business-logic")
include("adsCar-stubs")
include("ads-car-app-kafka")
include("adsCar-app-common")
