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
import kotlinx.ast.common.klass.KlassInheritance
import kotlinx.ast.common.klass.KlassString
import kotlinx.ast.grammar.kotlin.common.summary
import kotlinx.ast.grammar.kotlin.common.summary.Import
import kotlinx.ast.grammar.kotlin.common.summary.PackageHeader
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser

val scalarMap = mapOf(
    "String" to "string",
    "Int" to "number",
    "Boolean" to "boolean",
)

fun main() {
    val sourceDir = Path.of("/Users/mlo/git/kt2ts-sample/kotlin/src/main/kotlin/io/github/kt2tssample")
    val source = sourceDir.resolve("Sealed.kt")
    val destinationDir = sourceDir
    val destination = destinationDir.resolve("Sealed.ts")
    val astSource = AstSource.File(source.absolutePathString())
    val kotlinFile = KotlinGrammarAntlrKotlinParser.parseKotlinFile(astSource)
    kotlinFile
        .summary(attachRawAst = false)
        .onSuccess { astList ->
            val packageName = processPackage(astList)
            val imports = processImports(astList)
            val classes = process(astList).let(::addSealedChildClasses)
            val sb = StringBuilder()
            val classMap = classes.associateBy { it.localName }
            classes.forEach {
                sb.append("\n")
                if (it.isSealed) {
                    sb.append("export type ${it.localName.name} =")
                    it.sealedChildClasses.forEach { sb.append(" | ${it.name}") }
                    sb.append(";\n")
                } else {
                    sb.append("export interface ${it.localName.name} {\n")
                    val objectTypeProperty =
                        it.parentClasses
                            // TODO fails if more than one
                            .firstNotNullOfOrNull {
                                val p = classMap[it]
                                p?.annotations
                                    // TODO always qualifed...
                                    ?.filter { it.annotation.name == "JsonTypeInfo" }
                                    ?.map { it.values["property"] }
                                    ?.firstOrNull()
                            }
                    if (objectTypeProperty != null) {
                        sb.append("  $objectTypeProperty: \"${it.localName.name}\";\n")
                    }
                    it.fields.forEach { field ->
                        sb.append("  ${field.fieldName}: ${printType(field.type)};\n")
                    }
                    sb.append("}\n")
                }
            }
            destination.toFile().writeText(sb.toString())
        }
        .onFailure { errors -> errors.forEach(::println) }
}

fun printType(type: TypeDeclaration): String {
    val t = scalarMap[type.name.name] ?: type.name.name
    if (type.generics.isNotEmpty()) {
        return "$t<${type.generics.joinToString(separator = ", ") { printType(it) }}>"
    }
    return t
}
data class PackageDeclaration(val name: String)

// TODO think to subClass
data class ClassSimpleName(val name: String)

data class ClassQualifiedName(val name: String)

data class TypeDeclaration(val name: ClassSimpleName, val generics: List<TypeDeclaration>)

data class LocalClassDeclaration(
    val localName: ClassSimpleName,
    val qualifiedName: ClassQualifiedName,
)

data class FieldDeclaration(val fieldName: String, val type: TypeDeclaration)

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
    val sealedChildClasses: List<ClassSimpleName>,
    val parentClasses: List<ClassSimpleName>,
    val fields: List<FieldDeclaration>,
    // TODO pour choper la selection c'est recursif (attention boucles)
    val selectionOrigins: List<SelectionOrigin>,
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
            val annotations = processAnnotations(it.annotations)
            // TODO qualifiedName
            val selectionOrigins =
                if (annotations.any { it.annotation.name == "GenerateTypescript" }) {
                    listOf(SelectionOrigin.ByAnnotation(ClassSimpleName("GenerateTypescript")))
                } else {
                    emptyList()
                }
            val parentClasses = processParentClasses(it.children)
            val fields = processFields(it.parameter)
            ClassDeclaration(
                localName = ClassSimpleName(identifier),
                annotations = annotations,
                isSealed = modifiers.any { it.modifier == "sealed" },
                sealedChildClasses = emptyList(),
                parentClasses = parentClasses,
                fields = fields,
                selectionOrigins = selectionOrigins,
            )
        }

fun processParentClasses(children: List<Ast>): List<ClassSimpleName> =
    children.filterIsInstance<KlassInheritance>().mapNotNull {
        val identifier = it.type.identifier
        ClassSimpleName(identifier)
    }

fun processFields(declarations: List<KlassDeclaration>): List<FieldDeclaration> =
    declarations
        .find { it.keyword == "constructor" }
        ?.parameter
        ?.filterIsInstance<KlassDeclaration>()
        ?.mapNotNull {
            val identifier = it.identifier?.identifier ?: return@mapNotNull null
            val type = it.type?.firstOrNull()?.let { processTypeDeclaration(it) } ?: return@mapNotNull null
            FieldDeclaration(fieldName = identifier, type = type)
        } ?: emptyList()

fun processTypeDeclaration(type: KlassIdentifier): TypeDeclaration {
    val generics = type.attributes.filterIsInstance<KlassIdentifier>().map { processTypeDeclaration(it) }
    return TypeDeclaration(name = ClassSimpleName(type.identifier), generics = generics)
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
                                                    .filter {
                                                        it.description != "memberAccessOperator"
                                                    }
                                                    .flatMap {
                                                        it.children
                                                            .mapNotNull {
                                                                it as? DefaultAstTerminal
                                                            }
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

fun addSealedChildClasses(classes: List<ClassDeclaration>): List<ClassDeclaration> =
    classes.map { clazz ->
        if (clazz.isSealed) {
            // TODO use qualifiedName
            val sealedChildClasses =
                classes.filter { it.parentClasses.contains(clazz.localName) }.map { it.localName }
            clazz.copy(sealedChildClasses = sealedChildClasses)
        } else {
            clazz
        }
    }
