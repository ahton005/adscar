plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project
    val datetimeVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":adsCar-logging"))
    implementation(project(":adsCar-common"))
    implementation(project(":adsCar-log-models"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation(kotlin("test-junit"))
}

kotlin {
    sourceSets {
        all { languageSettings.optIn("kotlin.RequiresOptIn") }
    }
}
