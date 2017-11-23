package id.soetfw.vertx.extension

import io.vertx.core.MultiMap
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.awaitResult

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo
 */

suspend fun WebClient.get(absoluteURI: String, header: Map<String, String>): HttpResponse<Buffer> {
    return awaitResult {
        this.getAbs(absoluteURI).apply { headers().addAll(header) }.send(it)
    }
}

suspend fun WebClient.post(absoluteURI: String, body: JsonObject, header: Map<String, String> = emptyMap()): HttpResponse<Buffer> {
    return awaitResult {
        this.postAbs(absoluteURI).apply { headers().addAll(header) }.sendJsonObject(body, it)
    }
}

suspend fun WebClient.postForm(absoluteURI: String, header: Map<String, String> = emptyMap(), formData: Map<String, String>): HttpResponse<Buffer> {
    val payload = formData.toList().fold(MultiMap.caseInsensitiveMultiMap()) { form, (key, value) ->
        form.set(key, value)
    }

    return awaitResult {
        this.postAbs(absoluteURI).apply { headers().addAll(header) }.sendForm(payload, it)
    }
}

suspend fun <T : Any> HttpRequest<T>.sendJson(body: Any, header: Map<String, String> = emptyMap()): HttpResponse<T> {
    return awaitResult {
        headers().addAll(header)
        method(HttpMethod.POST)
        sendJson(body, it)
    }
}

suspend fun <T : Any> HttpRequest<T>.sendJsonObject(body: JsonObject, header: Map<String, String> = emptyMap()): HttpResponse<T> {
    return awaitResult {
        headers().addAll(header)
        method(HttpMethod.POST)
        sendJsonObject(body, it)
    }
}

inline fun <reified T : Any> HttpResponse<Buffer>.toValue(): T {
    return this.bodyAsJson(T::class.java)
}

fun HttpResponse<Buffer>.jsonBody(): JsonObject? {
    return this.bodyAsJsonObject()
}

fun HttpResponse<Buffer>.jsonArrayBody(): JsonArray? {
    return this.bodyAsJsonArray()
}

data class RequestException(override val message: String? = "", private val response: HttpResponse<*>, private val ex: Exception? = null) : RuntimeException(message, ex) {
    val statusCode = response.statusCode()
    val stringBody = response.bodyAsString()
    val jsonBody = response.bodyAsJsonObject()
}