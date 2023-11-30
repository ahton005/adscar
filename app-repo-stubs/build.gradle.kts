plugins {
    kotlin("jvm")
}

dependencies {
//    val coroutinesVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":adsCar-common"))
    implementation(project(":adsCar-stubs"))

// TODO    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation(project(":app-repo-tests"))
// TODO    testImplementation(kotlin("test-junit"))
}
