package id.soetfw.vertx.extension

import id.soetfw.vertx.security.SecurityUser
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Handler
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.StaticHandler

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

fun Route.first(): Route {
    return this.order(-100)
}

inline fun handler(crossinline handle: (RoutingContext) -> Unit): Handler<RoutingContext> {
    return Handler { handle(it) }
}


@Suppress("NOTHING_TO_INLINE")
inline fun Route.reroute(destination: String): Route {
    return this.handler { context ->
        context.reroute(destination)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun Route.serveStatic(): Route {
    return this.handler(StaticHandler.create())
}


@Suppress("NOTHING_TO_INLINE")
inline fun Route.serveStatic(webRoot: String): Route {
    return this.handler(StaticHandler.create().apply {
        setWebRoot(webRoot)
    })
}

/**
 * Extension to the HTTP response to output JSON objects.
 */
fun HttpServerResponse.endWithJson(obj: Any) {
    this.putHeader("Content-Type", "application/json; charset=utf-8")
            .end(Json.encodePrettily(obj))
}

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.header(key: String): String? {
    return this.request().headers().get(key)
}

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.param(key: String): String? {
    return this.request().getParam(key)
}

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.json(obj: Any) {
    val response = this.response()
    response.putHeader("Content-Type", "application/json; charset=utf-8")
            .putHeader("Access-Control-Allow-Origin", "*")
            .putHeader("Access-Control-Allow-Methods", "*")
            .putHeader("Access-Control-Allow-Credentials", "true")
            .end(Json.encode(obj))
}

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.text(obj: Any, contentType: String = "text/plain") {
    val response = this.response()
    response.putHeader("Content-Type", contentType)
            .end(Json.encode(obj))
}

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.json(headers: Map<String, String> = emptyMap(), message: Any) {
    this.response().apply {
        headers.entries.fold(this) { response, entries ->
            response.putHeader(entries.key, entries.value)
        }
        putHeader("Content-Type", "application/json; charset=utf-8")
        end(Json.encode(message))
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.jsonBody(): JsonObject? {
    return try {
        bodyAsJson
    } catch (e: Exception) {
        null
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.jsonArrayBody(): JsonArray? {
    return try {
        bodyAsJsonArray
    } catch (e: Exception) {
        null
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.OK(message: String = "", headers: Map<String, String> = emptyMap()) {
    this.response().let {
        it.statusCode = HttpResponseStatus.OK.code()
        headers.entries.fold(it) { response, entries ->
            response.putHeader(entries.key, entries.value)
        }
        it.end(message)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.prettyJson(obj: Any) {
    val response = this.response()
    response.putHeader("Content-Type", "application/json; charset=utf-8")
            .end(Json.encodePrettily(obj))
}


fun RoutingContext.principal(): JsonObject? {
    return this.user()?.principal()
}

fun RoutingContext.securityUser(): SecurityUser? {
    return this.user() as? SecurityUser
}

