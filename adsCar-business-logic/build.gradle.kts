plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":adsCar-common"))
    implementation(project(":adsCar-stubs"))
}
