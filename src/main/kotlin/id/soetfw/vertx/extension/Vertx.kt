package id.soetfw.vertx.extension

import id.soetfw.vertx.controller.Controller
import id.soetfw.vertx.value.DefaultErrorHandler
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpServer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.templ.TemplateEngine
import io.vertx.kotlin.config.ConfigRetrieverOptions
import io.vertx.kotlin.config.ConfigStoreOptions
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.async
import kotlin.coroutines.experimental.CoroutineContext

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo
 */


suspend fun Vertx.createHttpServer(router: Router, port: Int): HttpServer {
    val httpServer = this.createHttpServer()
            .requestHandler { request -> router.accept(request) }
    return awaitResult { httpServer.listen(port, it) }
}

suspend fun Vertx.deployVerticle(verticle: Verticle, config: JsonObject, worker: Boolean = false): String {
    val option = DeploymentOptions().apply {
        this.config = config
        this.isWorker = worker
    }

    return awaitResult { this.deployVerticle(verticle, option, it) }
}

suspend fun Vertx.retrieveConfig(vararg stores: ConfigStoreOptions): JsonObject {
    val sysConfig = ConfigStoreOptions(type = "sys")
    val envConfig = ConfigStoreOptions(type = "env")

    val options = ConfigRetrieverOptions(
            stores = stores.toList().plus(sysConfig).plus(envConfig)
    )

    val retriever = ConfigRetriever.create(this, options)
    return awaitResult { retriever.getConfig(it) }
}

suspend fun buildClustereVertx(option: VertxOptions): Vertx {
    return awaitResult { Vertx.clusteredVertx(option, it) }
}

fun buildVertx(option: VertxOptions): Vertx {
    return Vertx.vertx(option)
}

suspend fun buildVertx(): Vertx {
    return buildVertx(VertxOptions())
}

suspend fun RoutingContext.sendFile(fileName: String): Void? {
    return this.sendFile(fileName, 0)
}

suspend fun RoutingContext.sendFile(fileName: String, offset: Long): Void? {
    return this.sendFile(fileName, offset, Long.MAX_VALUE)
}

suspend fun RoutingContext.sendFile(fileName: String, offset: Long, length: Long): Void? {
    val response = this.response()
    return awaitResult { response.sendFile(fileName, offset, length, it) }
}

fun RoutingContext.getAuthorizationHeader(): String {

    val authorization = this.request().headers().get(HttpHeaders.AUTHORIZATION) ?: throw SecurityException("Authorization header is required")
    val splittedAuth = authorization.split(" ")

    val authorizationScheme = splittedAuth[0]
    val authorizationToken = splittedAuth[1]

    if (authorizationScheme != "Bearer") throw SecurityException("Invalid authorization scheme")
    return authorizationToken

}

fun RoutingContext.getRemoteIpAddress(): String {
    return this.request()?.remoteAddress()?.host() ?: ""
}

fun RoutingContext.getBrowserInfo(): String = this.request()?.headers()?.get("User-Agent") ?: ""

fun Route.asyncHandler(coroutineContext: CoroutineContext? = null, errorHandler: ErrorHandler = DefaultErrorHandler, handler: suspend RoutingContext.() -> Unit): Route {
    return this.handler { routingContext ->
        val context = coroutineContext ?: routingContext.vertx().dispatcher()
        async(context) {
            try {
                handler(routingContext)
            } catch (e: Exception) {
                errorHandler(routingContext, e)
            }
        }
    }
}

fun Route.jsonHandler(coroutineContext: CoroutineContext? = null, errorHandler: ErrorHandler = DefaultErrorHandler, handler: suspend RoutingContext.() -> Any): Route {
    return this.handler { routingContext ->
        val context = coroutineContext ?: routingContext.vertx().dispatcher()
        async(context) {
            try {
                routingContext.json(handler(routingContext))
            } catch (e: Exception) {
                errorHandler(routingContext, e)
            }
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
fun Route.templateHandler(coroutineContext: CoroutineContext? = null, errorHandler: ErrorHandler = DefaultErrorHandler, engine: TemplateEngine, templateName: String): Route {
    return this.asyncHandler(coroutineContext, errorHandler) {
        this.text(render(engine, templateName))
    }
}

suspend fun RoutingContext.render(engine: TemplateEngine, templateName: String): Buffer {
    return awaitResult { engine.render(this, "", templateName, it) }
}

fun Router.subRoute(path: String, subController: Controller): Router {
    return this.mountSubRouter(path, subController.create())
}

typealias ErrorHandler = (RoutingContext, Throwable) -> Unit
