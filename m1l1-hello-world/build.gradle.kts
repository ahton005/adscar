plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

application {
    mainClass.set("ru.otus.otuskotlin.marketplace.MainKt")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "ru.otus.otuskotlin.marketplace.MainKt"
    }
}
