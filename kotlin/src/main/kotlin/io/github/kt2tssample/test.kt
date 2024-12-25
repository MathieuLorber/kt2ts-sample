package io.github.kt2tssample

import com.intellij.openapi.Disposable
import org.jetbrains.kotlin.analyzer.ResolverForSingleModuleProject
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.readText

fun main() {
    val configuration = CompilerConfiguration()
    val environment: KotlinCoreEnvironment = KotlinCoreEnvironment.createForProduction(
        parentDisposable = ResolverForSingleModuleProject(
            debugName = "test",
            projectContext = TODO(),
            module = TODO(),
            resolverForModuleFactory = TODO(),
            searchScope = TODO(),
            builtIns = TODO(),
            languageVersionSettings = TODO(),
            syntheticFiles = TODO(),
            sdkDependency = TODO(),
            knownDependencyModuleDescriptors = TODO()
        ),
        configuration = configuration,
        configFiles = EnvironmentConfigFiles.JVM_CONFIG_FILES
    )
    val psiFactory: KtPsiFactory = KtPsiFactory(environment.project)
    val path = Path.of("/Users/mlo/git/kt2ts-sample/kotlin/src/main/kotlin/io/github/kt2tssample/Sealed.kt")
    val ktFile = psiFactory.createFile(path.absolutePathString(), path.readText(Charsets.UTF_8))
    println(ktFile)
}