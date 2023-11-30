plugins {
    kotlin("jvm")
}

dependencies {
    val cache4kVersion: String by project
    val coroutinesVersion: String by project
    val kmpUUIDVersion: String by project

    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":adsCar-common"))
    implementation(project(":app-repo-tests"))

    implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("com.benasher44:uuid:$kmpUUIDVersion")

    testImplementation("junit:junit:4.13.2")
}
