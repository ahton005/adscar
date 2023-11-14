plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("ru.otus.otuskotlin.marketplace.app.kafka.MainKt")
}

dependencies {
    val kafkaVersion: String by project
    val coroutinesVersion: String by project
    val atomicfuVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:atomicfu:$atomicfuVersion")

    // log
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    // transport models
    implementation(project(":adsCar-common"))
    implementation(project(":adsCar-api-v1-jackson"))
    implementation(project(":adsCar-mappers-v1"))
    // logic
    implementation(project(":adsCar-business-logic"))

    implementation(project(":adsCar-app-common"))
    implementation(project(":adsCar-logging"))

    testImplementation(kotlin("test-junit"))
}
