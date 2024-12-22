plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

val kotlinVersion = "2.1.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://mlorber.net/maven_repo")
}

configurations.all { exclude(group = "junit", module = "junit") }

ksp {
    arg("kt2ts:clientDirectory", "$rootDir/typescript")
    arg("kt2ts:dropPackage", "com.kttswebapptemplate")
    arg("kt2ts:mappings", "$rootDir/typescript/kt-to-ts-mappings.json")
    arg(
        "kt2ts:nominalStringMappings",
        listOf(
            "io.github.kt2tssample.SampleId"
        )
            .joinToString(separator = "|"))
    arg("kt2ts:mapClass", "Dict")
    arg("kt2ts:mapClassFile", "utils/nominal-class.ts")
    arg("kt2ts:prettierDependencyInstall", "yarn")
    arg("kt2ts:prettierBinary", "node_modules/prettier/bin/prettier.cjs")
    arg("kt2ts:absoluteImport", "true")
    arg("kt2ts:debugFile", "$rootDir/typescript/build/debug-generation.html")
}

dependencies {
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    // kt2ts
    implementation("io.github.kt2ts:kt2ts-annotation:1.0.0")
    ksp("io.github.kt2ts:kt2ts-ksp-generator:0.0.9")

    // utils
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
}
