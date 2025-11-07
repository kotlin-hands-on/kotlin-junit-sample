group = "org.jetbrains.kotlin"
version = "1.0-SNAPSHOT"
description = "kotlin-junit-complete"
java.sourceCompatibility = JavaVersion.VERSION_17

plugins {
    application
    kotlin("jvm") version "2.2.21"
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("org.jetbrains.kotlin.junit.App")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.gitlab.klamonte:jexer:1.6.0")

    testImplementation(kotlin("test"))
    testImplementation(libs.org.junit.jupiter.junit.jupiter.api)
    testImplementation(libs.org.junit.jupiter.junit.jupiter.params)
    testRuntimeOnly(libs.org.junit.jupiter.junit.jupiter.engine)
    testRuntimeOnly(libs.org.junit.platform.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
