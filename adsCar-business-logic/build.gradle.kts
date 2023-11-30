plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":adsCar-common"))
    implementation(project(":adsCar-stubs"))
    implementation(project(":app-lib-cor"))
    implementation(project(":adsCar-logging"))

    implementation(project(":app-repo-tests"))
    implementation(project(":app-repo-stubs"))
    implementation(project(":app-repo-in-memory"))

    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
}
