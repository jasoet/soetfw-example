package id.soetfw.vertx.extension

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

/**
 * Execute command in form of List<String>.
 * Able to handle standard input source from File, InputStream and String and able to handle standard output source to File and OutputStream
 * This function is suspendable.
 *
 * @param input Standard input for command, able to receive File, InputStream and String input.
 * @param output Standard output for command, able to send output to File and OutputStream.
 */

suspend fun List<String>.execute(input: Any? = null, output: Any? = null): Int {
    val log = logger("CommandExtension")

    log.debug("Command to Execute ${this.joinToString(" ")}")

    val tmpDir: String = System.getProperty("java.io.tmpdir")

    val processBuilder = ProcessBuilder(this)

    when (input) {
        is File -> {
            log.debug("Accept File Input")
            processBuilder.redirectInput(input)
        }
        is InputStream -> {
            log.debug("Accept InputStream Input")
            val inputFile = File(tmpDir, UUID.randomUUID().toString())
            FileUtils.copyInputStreamToFile(input, inputFile)
            processBuilder.redirectInput(inputFile)
        }
        is String -> {
            log.debug("Accept String Input")
            val inputFile = File(tmpDir, UUID.randomUUID().toString())
            FileUtils.writeStringToFile(inputFile, input, "UTF-8")
            processBuilder.redirectInput(inputFile)
        }
    }

    return when (output) {
        is File -> {
            processBuilder.redirectOutput(output)
            processBuilder.start().waitFor()
        }
        is OutputStream -> {
            val inputFile = File(tmpDir, UUID.randomUUID().toString())
            processBuilder.redirectOutput(inputFile)
            val exitCode = processBuilder.start().waitFor()

            FileInputStream(inputFile).use { fis ->
                output.use {
                    IOUtils.copy(fis, output)
                }
            }

            exitCode
        }
        else -> {
            processBuilder.start().waitFor()
        }
    }
}


