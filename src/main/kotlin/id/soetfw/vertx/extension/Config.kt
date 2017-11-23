package id.soetfw.vertx.extension

import io.vertx.config.ConfigStoreOptions
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.config.ConfigStoreOptions
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import kotlin.reflect.full.isSubclassOf

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo
 */
fun propertiesConfig(path: String): ConfigStoreOptions {
    return ConfigStoreOptions(
            type = "file",
            format = "properties",
            config = json {
                obj("path" to path)
            }
    )
}

fun jsonConfig(path: String): ConfigStoreOptions {
    return ConfigStoreOptions(
            type = "file",
            format = "json",
            config = json {
                obj("path" to path)
            }
    )
}

fun JsonObject.getStringExcept(key: String, exceptionMessage: String): String {
    return this.getString(key) ?: throw IllegalArgumentException(exceptionMessage)
}

inline fun <reified T : Any> JsonObject.getExcept(key: String, exceptionMessage: (String) -> String): T {
    return when {
        T::class.isSubclassOf(String::class) -> this.getString(key) as T? ?: throw IllegalArgumentException(exceptionMessage(key))
        T::class.isSubclassOf(Int::class) -> this.getInteger(key) as T? ?: throw IllegalArgumentException(exceptionMessage(key))
        T::class.isSubclassOf(Double::class) -> this.getDouble(key) as T? ?: throw IllegalArgumentException(exceptionMessage(key))
        T::class.isSubclassOf(Boolean::class) -> this.getBoolean(key) as T? ?: throw IllegalArgumentException(exceptionMessage(key))
        T::class.isSubclassOf(JsonObject::class) -> this.getJsonObject(key) as T? ?: throw IllegalArgumentException(exceptionMessage(key))
        T::class.isSubclassOf(JsonArray::class) -> this.getJsonArray(key) as T? ?: throw IllegalArgumentException(exceptionMessage(key))
        T::class.isSubclassOf(ByteArray::class) -> this.getBinary(key) as T? ?: throw IllegalArgumentException(exceptionMessage(key))
        else -> throw IllegalArgumentException("${T::class.qualifiedName} Not Supported")
    }
}



