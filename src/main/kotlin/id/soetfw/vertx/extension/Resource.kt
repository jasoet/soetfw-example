package id.soetfw.vertx.extension

import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import java.io.InputStreamReader
import java.lang.IllegalArgumentException
import java.net.ServerSocket
import java.nio.charset.StandardCharsets
import kotlin.reflect.full.isSubclassOf

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

inline fun <reified T : Any> env(key: String, defaultValue: T? = null): T {
    val value: String? = System.getenv(key)
    return if (value != null) {
        when {
            T::class.isSubclassOf(String::class) -> value as T
            T::class.isSubclassOf(Int::class) -> value.toInt() as T
            T::class.isSubclassOf(Double::class) -> value.toDouble() as T
            T::class.isSubclassOf(Boolean::class) -> value.toBoolean() as T
            else -> throw IllegalArgumentException("${T::class.qualifiedName} Not Supported")
        }
    } else defaultValue ?: throw IllegalArgumentException("Illegal: $key not found and default value is null!")
}

inline fun <reified T : Any> applyEnv(key: String, defaultValue: T? = null, operation: (T) -> Unit) {
    val logger = logger(System::class)
    try {
        val value = env(key, defaultValue)
        operation(value)
    } catch (e: Exception) {
        logger.info("Exception occurred ${e.message}, Operation ignored!")
    }
}

fun String.resourceToBuffer(): Buffer {
    val inputStream = javaClass.getResourceAsStream(this)
    val byteArray = ByteArray(inputStream.available())

    inputStream.use {
        it.read(byteArray)
    }
    return Buffer.buffer(byteArray)
}

fun String.toJsonObject(): JsonObject {
    val logger = logger(JsonObject::class)
    return try {
        val inputStream = InputStreamReader(javaClass.getResourceAsStream(this), StandardCharsets.UTF_8)
        val jsonString = inputStream.useLines { it.joinToString("") }
        logger.info("Load config from $this")
        JsonObject(jsonString)
    } catch (e: Exception) {
        logger.info("Config Cannot Loaded, Return Empty JsonObject.  Cause: ${e.message}")
        JsonObject()
    }
}

fun tmpDir(): String {
    return System.getProperty("java.io.tmpdir")
}

fun homeDir(): String {
    return System.getProperty("user.home")
}

fun randomPort(): Int {
    val socket = ServerSocket(0)
    val port = socket.localPort
    socket.close()
    return port
}