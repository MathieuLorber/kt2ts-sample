plugins {
    val kotlinVersion = "2.1.0"
    kotlin("jvm") version kotlinVersion apply false
}

buildscript {
    val kotlinVersion = "2.1.0"
    dependencies { classpath(kotlin("gradle-plugin", version = kotlinVersion)) }
}
