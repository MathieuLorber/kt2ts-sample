package io.github.kt2tssample

import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlinx.ast.common.AstSource
import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.DefaultAstNode
import kotlinx.ast.common.ast.DefaultAstTerminal
import kotlinx.ast.common.klass.KlassAnnotation
import kotlinx.ast.common.klass.KlassDeclaration
import kotlinx.ast.common.klass.KlassIdentifier
import kotlinx.ast.common.klass.KlassString
import kotlinx.ast.common.klass.expressions
import kotlinx.ast.common.klass.identifierName
import kotlinx.ast.grammar.kotlin.common.summary
import kotlinx.ast.grammar.kotlin.common.summary.Import
import kotlinx.ast.grammar.kotlin.common.summary.PackageHeader
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser

fun main() {
  val dir = Path.of("/Users/mlo/git/kt2ts-sample/kotlin/src/main/kotlin/io/github/kt2tssample")
  val path = dir.resolve("Sealed.kt")
  val source = AstSource.File(path.absolutePathString())
  val kotlinFile = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source)
  kotlinFile
      .summary(attachRawAst = false)
      .onSuccess { astList ->
        val packageName = processPackage(astList)
        val imports = processImports(astList)
        val classes = process(astList)
        val sb = StringBuilder()
        classes.forEach {
          if (it.isSealed) {
            sb.append("export type ${it.localName.name} = ")
            it.sealedSubClasses.forEach { sb.append("| ${it.name}") }
            sb.append(";\n")
          }
        }
      }
      .onFailure { errors -> errors.forEach(::println) }
}

data class PackageDeclaration(val name: String)

// TODO think to subClass
data class ClassSimpleName(val name: String)

data class ClassQualifiedName(val name: String)

data class LocalClassDeclaration(
    val localName: ClassSimpleName,
    val qualifiedName: ClassQualifiedName
)

data class FieldDeclaration(val fieldName: String, val type: ClassSimpleName)

// TODO attention pour les values il faut faire avec import
data class ClassAnnotation(val annotation: ClassSimpleName, val values: Map<String, String>)

sealed class SelectionOrigin {
  data class ByAnnotation(val annotation: ClassSimpleName) : SelectionOrigin()

  data class ByParent(val annotation: ClassSimpleName) : SelectionOrigin()
}

data class ClassDeclaration(
    val localName: ClassSimpleName,
    val annotations: List<ClassAnnotation>,
    val isSealed: Boolean,
    val sealedSubClasses: List<ClassSimpleName>,
    val parentClasses: List<ClassSimpleName>,
    val fields: List<FieldDeclaration>,
    val selectionOrigins: List<SelectionOrigin>
)

private fun processPackage(asts: List<Ast>): PackageDeclaration =
    asts.filterIsInstance<PackageHeader>().firstOrNull().let {
      PackageDeclaration(it?.identifier?.joinToString(separator = ".") { it.identifier } ?: "")
    }

private fun processImports(asts: List<Ast>): Map<ClassSimpleName, LocalClassDeclaration> =
    asts
        .filterIsInstance<DefaultAstNode>()
        .find { it.description == "importList" }
        .let { it?.children ?: emptyList() }
        .filterIsInstance<Import>()
        .associate {
          val localName = ClassSimpleName(it.alias?.identifier ?: it.identifier.last().identifier)
          val qualifiedName =
              ClassQualifiedName(it.identifier.joinToString(separator = ".") { it.identifier })
          localName to LocalClassDeclaration(localName, qualifiedName)
        }

private fun process(asts: List<Ast>): List<ClassDeclaration> =
    asts
        .filterIsInstance<KlassDeclaration>()
        // en fait on garde d'autres trucs, genre les sealed
        //        .filter {it.children.filterIsInstance<KlassModifier>().first().modifier == "data"}
        .mapNotNull {
          val identifier = it.identifier?.identifier ?: return@mapNotNull null
          val modifiers = it.modifiers.filter { it.modifier in setOf("sealed", "data") }
          if (modifiers.isEmpty()) return@mapNotNull null
          //            val parents =
          ClassDeclaration(
              localName = ClassSimpleName(identifier),
              annotations = processAnnotations(it.annotations),
              isSealed = modifiers.any { it.modifier == "sealed" },
              sealedSubClasses = emptyList(),
              parentClasses = emptyList(),
              fields =
                  it.parameter.mapNotNull {
                    val fieldName = it.identifier?.identifier ?: ""
                    val type =
                        it.type?.let {
                          val identifier = it.identifierName()
                          ClassSimpleName(identifier)
                        } ?: return@mapNotNull null
                    FieldDeclaration(fieldName, type)
                  },
              selectionOrigins = emptyList(),
          )
        }

fun processAnnotations(annotations: List<KlassAnnotation>): List<ClassAnnotation> =
    annotations.mapNotNull {
      val identifier = it.identifier.firstOrNull()?.identifier ?: return@mapNotNull null
      val values =
          it.arguments
              .filter { it.keyword == "argument" }
              .mapNotNull {
                val identifier = it.identifier?.identifier ?: return@mapNotNull null
                val value =
                    it.expressions.firstOrNull()?.let {
                      when (it) {
                        is KlassIdentifier -> {
                          val values =
                              it.children.filterIsInstance<DefaultAstNode>().flatMap {
                                it.children.filterIsInstance<DefaultAstNode>().flatMap {
                                  it.children
                                      .filterIsInstance<DefaultAstNode>()
                                      .filter { it.description != "memberAccessOperator" }
                                      .flatMap {
                                        it.children
                                            .mapNotNull { it as? DefaultAstTerminal }
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
    }
