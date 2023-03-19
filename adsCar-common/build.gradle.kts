plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}

    sourceSets {
        val datetimeVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))

                api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}