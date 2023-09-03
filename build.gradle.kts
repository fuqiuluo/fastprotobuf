val kotlin_version: String by project

plugins {
    kotlin("jvm") version "1.9.10"
}

group = "moe.fuqiuluo"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.google.protobuf:protobuf-java:3.24.0")
}
