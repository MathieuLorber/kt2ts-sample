package io.github.kt2tssample

import io.kotest.matchers.shouldBe
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.collections.minus
import kotlin.io.path.absolutePathString
import kotlin.io.path.name
import kotlin.io.path.readText
import org.junit.jupiter.api.Test

internal class GenerationTest {

    companion object {
        const val noGenerationKotlinFile = "NoGeneration.kt"
        const val kotlinExtension = ".kt"
        const val expectedTypescriptExtension = ".expected.ts"
        const val generatedTypescriptExtension = ".generated.ts"
        const val expectedDir = "kotlin/src/test/resources/expected"
        const val generatedDir = "typescript/src/generated"
    }

    val projectDir by lazy { Paths.get(System.getProperty("user.dir")).parent }

    val kotlinFiles by lazy {
        val dir = projectDir.resolve("kotlin/src/main/kotlin")
        val dirLength = dir.absolutePathString().length + 1
        Files.walk(dir)
            .filter { it.toString().endsWith(kotlinExtension) }
            .filter { it.name != noGenerationKotlinFile }
            .toList()
            .map { it.absolutePathString().substring(dirLength) }
            .map { it.dropLast(kotlinExtension.length) }
    }

    val expectedFiles by lazy {
        val dir = projectDir.resolve(expectedDir)
        val dirLength = dir.absolutePathString().length + 1
        Files.walk(dir)
            .filter { it.toString().endsWith(expectedTypescriptExtension) }
            .toList()
            .map { it.absolutePathString().substring(dirLength) }
            .map { it.dropLast(expectedTypescriptExtension.length) }
    }

    val generatedTypescriptFiles by lazy {
        val dir = projectDir.resolve(generatedDir)
        val dirLength = dir.absolutePathString().length + 1
        Files.walk(dir)
            .filter { it.toString().endsWith(generatedTypescriptExtension) }
            .toList()
            .map { it.absolutePathString().substring(dirLength) }
            .map { it.dropLast(generatedTypescriptExtension.length) }
    }

    @Test
    fun `expected files are same than kotlin`() {
        (kotlinFiles - expectedFiles).let {
            require(it.isEmpty()) { "Missing in expected files: $it" }
        }
        (expectedFiles - kotlinFiles).let {
            require(it.isEmpty()) { "Files in \"expected\" dir not in kotlin dir: $it" }
        }
    }

    @Test
    fun `generated files are same than kotlin`() {
        (kotlinFiles - generatedTypescriptFiles).let {
            require(it.isEmpty()) { "Missing in generated files: $it" }
        }
        (generatedTypescriptFiles - kotlinFiles).let {
            require(it.isEmpty()) { "Generated files not in kotlin dir: $it" }
        }
    }

    @Test
    fun `check generation is ok`() {
        expectedFiles.forEach {
            val expected =
                projectDir
                    .resolve(expectedDir)
                    .resolve("$it$expectedTypescriptExtension")
                    .readText(Charsets.UTF_8)
            val generated =
                projectDir
                    .resolve(generatedDir)
                    .resolve("$it$generatedTypescriptExtension")
                    .readText(Charsets.UTF_8)
            generated shouldBe expected
        }
    }
}
