plugins {
    kotlin("jvm") version "2.4.10"
    application
}

group = "til.kotlin"
version = "0.1.0"

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

