plugins {
    kotlin("jvm")
}

repositories {
//    зачем здесь писать если прописано в главном конфиге?
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test-junit"))
}