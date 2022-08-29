import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "de.fruxz"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")

    implementation("com.github.thefruxz.moltenkt:moltenkt-core:1.0-PRE-16")
    implementation("com.github.thefruxz.moltenkt:moltenkt-paper:1.0-PRE-16")
    implementation("com.github.thefruxz.moltenkt:moltenkt-unfold:1.0-PRE-16")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}