package io.github.kt2tssample

import java.io.InputStream
import java.nio.file.Path
import java.util.Scanner

object ShellRunner {

    data class CommandResult(
        val result: Int,
        val output: List<String>,
        val errorOutput: List<String>
    )

    fun run(command: String, vararg params: String): CommandResult = doRun(null, command, *params)

    fun run(directory: Path, command: String, vararg params: String): CommandResult =
        doRun(directory, command, *params)

    private fun doRun(directory: Path?, command: String, vararg params: String): CommandResult {
        val builder =
            ProcessBuilder().apply {
                environment().apply {
                    val addToPath =
                        listOf(
                            "/usr/local/bin",
                            "/opt/homebrew/opt/postgresql@15/bin",
                            "/usr/local/opt/postgresql@15/bin",
                            "/opt/homebrew/bin"
                        )
                    put("PATH", "${get("PATH")}:${addToPath.joinToString(separator = ":")}")
                }
                if (directory != null) {
                    directory(directory.toFile())
                }
            }
        val fullCommand = command + params.fold("") { acc, s -> "$acc $s" }
        println("Run '$fullCommand' ${if (directory != null) "in $directory" else ""}")
        builder.command("sh", "-c", fullCommand)
        val process = builder.start()
        val (output, outputThread) = outputThread(process.inputStream, "Command output:")
        val (error, errorThread) = outputThread(process.errorStream, "Command error:")
        val result = process.waitFor()
        outputThread.join()
        errorThread.join()
        println("Command result: $result")
        return CommandResult(result, output, error)
    }

    private fun outputThread(
        inputStream: InputStream,
        logPrefix: String
    ): Pair<List<String>, Thread> {
        val result = mutableListOf<String>()
        val t =
            Thread {
                val s = Scanner(inputStream)
                while (s.hasNextLine()) {
                    val l = s.nextLine()
                    //logger.debug { "$logPrefix $l" }
                    result.add(l)
                }
            }
                .apply { start() }
        return result to t
    }
}