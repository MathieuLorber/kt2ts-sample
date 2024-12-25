package io.github.kt2tssample

import kastree.ast.psi.Converter
import kastree.ast.psi.Parser

fun main() {
    val code = """
    package foo

    fun bar() {
        // Print hello
        println("Hello, World!")
    }

    fun baz() = println("Hello, again!")
""".trimIndent()
// Call the parser with the code
    val extrasMap = Converter.WithExtras()
    val file = Parser(extrasMap).parseFile(code)
// The file var is now a kastree.ast.Node.File that is used in future examples...
}