plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":adsCar-api-v1-jackson"))
    implementation(project(":adsCar-common"))

    testImplementation(kotlin("test-junit"))
}