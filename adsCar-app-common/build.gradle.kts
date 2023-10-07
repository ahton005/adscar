plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))
    // transport models
    implementation(project(":adsCar-api-v1-jackson"))
    implementation(project(":adsCar-mappers-v1"))
    // commons
    implementation(project(":adsCar-common"))
    // Stubs
    implementation(project(":adsCar-stubs"))
    // Biz
    implementation(project(":adsCar-business-logic"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

    testImplementation(kotlin("test-junit"))
}
