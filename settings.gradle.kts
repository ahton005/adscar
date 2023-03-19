rootProject.name = "ZyablovAdsCar"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openApiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion apply false
        id("io.kotest.multiplatform") version kotestVersion apply false
        id("org.openapi.generator") version openApiVersion apply false
    }
}

// include("m1l1-quickstart")
include("m1l1-hello-world")
// include("m1l3-oop")
// include("m1l4-dsl")
// include("m1l5-coroutines")
// include("m1l6-flows-and-channels")
// include("m1l7-kmp")
// include("m2l2-testing")
include("adsCar-api-v1-jackson")
include("adsCar-common")
include("adsCar-mappers-v1")
