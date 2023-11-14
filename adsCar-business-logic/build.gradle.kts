plugins {
    kotlin("jvm")
}

kotlin {
    sourceSets {
        all { languageSettings.optIn("kotlin.RequiresOptIn") }
    }
}

dependencies {
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":adsCar-common"))
    implementation(project(":adsCar-stubs"))
    implementation(project(":app-lib-cor"))
    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
}
