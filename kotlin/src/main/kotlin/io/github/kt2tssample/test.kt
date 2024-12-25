package io.github.kt2tssample

import kotlinx.ast.common.AstSource
import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.DefaultAstNode
import kotlinx.ast.common.klass.KlassDeclaration
import kotlinx.ast.common.klass.KlassIdentifier
import kotlinx.ast.common.klass.KlassString
import kotlinx.ast.common.klass.expressions
import kotlinx.ast.common.print
import kotlinx.ast.grammar.kotlin.common.summary
import kotlinx.ast.grammar.kotlin.common.summary.Import
import kotlinx.ast.grammar.kotlin.common.summary.PackageHeader
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlinx.ast.common.ast.DefaultAstTerminal

fun main() {
    val path = Path.of("/Users/mlo/git/kt2ts-sample/kotlin/src/main/kotlin/io/github/kt2tssample/Sealed.kt")
    val source = AstSource.File(path.absolutePathString())
    val kotlinFile = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source)
    kotlinFile.summary(attachRawAst = false).onSuccess { astList ->
        val packageName = processPackage(astList)
        val imports = processImports(astList)
        val classes = process(astList)
        classes.forEach {
            it.annotations.forEach {
                println(it)
            }
        }
    }.onFailure { errors ->
        errors.forEach(::println)
    }
}

data class PackageDeclaration(val name: String)

// TODO think to subClass
data class ClassSimpleName(val name: String)

data class ClassQualifiedName(val name: String)

data class LocalClassDeclaration(val localName: ClassSimpleName, val qualifiedName: ClassQualifiedName)

data class FieldDeclaration(val fieldName: String, val type: LocalClassDeclaration)

// TODO attention pour les values il faut faire avec import
data class ClassAnnotation(val annotation: ClassSimpleName, val values: Map<String, String>)

sealed class SelectionOrigin {
    data class ByAnnotation(val annotation: LocalClassDeclaration) : SelectionOrigin()

    data class ByParent(val annotation: LocalClassDeclaration) : SelectionOrigin()
}

data class ClassDeclaration(
    val localName: ClassSimpleName,
    val annotations: List<ClassAnnotation>,
    val isSealed: Boolean,
    // TODO alimenter dans une seconde passe
    val sealedSubClasses: List<ClassSimpleName>,
    // TODO penser à rajouter info par annotation
    val fields: List<FieldDeclaration>,
    val selectionOrigins: List<SelectionOrigin>
)

private fun processPackage(asts: List<Ast>): PackageDeclaration = asts.filterIsInstance<PackageHeader>().firstOrNull()
    .let { PackageDeclaration(it?.identifier?.joinToString(separator = ".") { it.identifier } ?: "") }

private fun processImports(asts: List<Ast>): Map<ClassSimpleName, LocalClassDeclaration> =
    asts
        .filterIsInstance<DefaultAstNode>()
        .find { it.description == "importList" }
        .let { it?.children ?: emptyList() }
        .filterIsInstance<Import>()
        .associate {
            val localName =
                ClassSimpleName(it.alias?.identifier ?: it.identifier.last().identifier)
            val qualifiedName = ClassQualifiedName(it.identifier.joinToString(separator = ".") { it.identifier })
            localName to LocalClassDeclaration(
                localName, qualifiedName
            )
        }

private fun process(
    asts: List<Ast>
): List<ClassDeclaration> =
    asts.filterIsInstance<KlassDeclaration>().mapNotNull {
        val identifier = it.identifier?.identifier
            ?: return@mapNotNull null
        ClassDeclaration(
            localName = ClassSimpleName(identifier),
            annotations = it.annotations.mapNotNull {
                val identifier = it.identifier.firstOrNull()?.identifier
                    ?: return@mapNotNull null
                val values = it.arguments
                    .filter { it.keyword == "argument" }
                    .mapNotNull {
                        val identifier = it.identifier?.identifier
                            ?: return@mapNotNull null
                        val value = it.expressions.firstOrNull()?.let {
                            when (it) {
                                is KlassIdentifier -> {
                                    val values = it.children.filterIsInstance<DefaultAstNode>().flatMap {
                                        it.children.filterIsInstance<DefaultAstNode>().flatMap {
                                            it.children.filterIsInstance<DefaultAstNode>()
                                                .filter { it.description != "memberAccessOperator" }
                                                .flatMap {
                                                    it.children.mapNotNull { it as? DefaultAstTerminal }
                                                        ?.map { it.text } ?: emptyList()
                                                }
                                        }
                                    }
                                    (listOf(it.identifier) + values).joinToString(separator = ".")
                                }

                                is KlassString -> it.children.first().description.replace("\"", "")
                                else -> null
                            }
                        } ?: return@mapNotNull null
                        identifier to value
                    }
                    .toMap()
                ClassAnnotation(ClassSimpleName(identifier), values)
            },
            isSealed = it.modifiers.any { it.modifier == "sealed" },
            sealedSubClasses = emptyList(),
            fields = emptyList(),
            selectionOrigins = emptyList(),
        )
    }
