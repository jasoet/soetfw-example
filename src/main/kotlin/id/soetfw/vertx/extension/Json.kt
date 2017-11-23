package id.soetfw.vertx.extension

import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import kotlin.reflect.full.isSubclassOf

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */
fun Any?.toJson(): JsonObject {
    if (this == null) return JsonObject()
    return JsonObject(Json.mapper.writeValueAsString(this))
}

@Suppress("NOTHING_TO_INLINE")
inline fun jsonObject(vararg fields: Pair<String, Any?>): JsonObject = JsonObject(fields.toMap())

inline fun <reified T : Any> JsonObject.mapTo(): T {
    return this.mapTo(T::class.java)
}

inline fun <reified T : Any> JsonObject?.toValue(): T? {
    return try {
        this?.mapTo(T::class.java)
    } catch (ie: IllegalArgumentException) {
        val log = logger("Json Extension")
        log.warn(ie.message, ie)
        null
    }
}

fun JsonObject?.getString(key: String): String? {
    return this?.getString(key)
}

fun JsonObject?.getString(key: String, default: String): String {
    return this?.getString(key, default) ?: default
}

fun JsonObject?.getBoolean(key: String): Boolean? {
    return this?.getBoolean(key)
}

fun JsonObject?.getBoolean(key: String, default: Boolean): Boolean {
    return this?.getBoolean(key, default) ?: default
}

fun JsonObject?.getInteger(key: String): Int? {
    return this?.getInteger(key)
}

fun JsonObject?.getInteger(key: String, default: Int): Int {
    return this?.getInteger(key, default) ?: default
}

fun JsonObject?.getLong(key: String): Long? {
    return this?.getLong(key)
}

fun JsonObject?.getLong(key: String, default: Long): Long {
    return this?.getLong(key, default) ?: default
}

fun JsonObject?.getByteArray(key: String): ByteArray? {
    return this?.getByteArray(key)
}

fun JsonObject?.getByteArray(key: String, default: ByteArray): ByteArray {
    return this?.getByteArray(key, default) ?: default
}

fun JsonObject?.getDouble(key: String): Double? {
    return this?.getDouble(key)
}

fun JsonObject?.getDouble(key: String, default: Double): Double {
    return this?.getDouble(key, default) ?: default
}

fun JsonObject?.getFloat(key: String): Float? {
    return this?.getFloat(key)
}

fun JsonObject?.getFloat(key: String, default: Float): Float {
    return this?.getFloat(key, default) ?: default
}

fun JsonObject?.getJsonObject(key: String): JsonObject? {
    return this?.getJsonObject(key)
}

fun JsonObject?.getJsonObject(key: String, default: JsonObject): JsonObject {
    return this?.getJsonObject(key, default) ?: default
}

fun JsonObject?.getJsonArray(key: String): JsonArray? {
    return this?.getJsonArray(key)
}

fun JsonObject?.getJsonArray(key: String, default: JsonArray): JsonArray {
    return this?.getJsonArray(key, default) ?: default
}

fun JsonObject?.getValue(key: String): Any? {
    return this?.getValue(key)
}

fun JsonObject?.getValue(key: String, default: Any): Any {
    return this?.getValue(key, default) ?: default
}

fun JsonObject?.toMap(): Map<String, Any?> {
    return this?.map ?: emptyMap()
}

inline fun <reified T : Any> JsonArray?.asList(): List<T> {
    if (this == null) return emptyList()
    val ops: (Any) -> T = when {
        T::class.isSubclassOf(String::class) -> { t -> t as T }
        T::class.isSubclassOf(Int::class) -> { t -> t as T }
        T::class.isSubclassOf(Double::class) -> { t -> t as T }
        T::class.isSubclassOf(Boolean::class) -> { t -> t as T }
        T::class.isSubclassOf(JsonObject::class) -> { t -> t as T }
        T::class.isSubclassOf(JsonArray::class) -> { t -> t as T }
        else -> { t -> (t as JsonObject).mapTo() }
    }
    return this.map { ops(it) }
}
