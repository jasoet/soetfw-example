package id.soetfw.vertx.value

import id.soetfw.vertx.extension.BadRequestException
import id.soetfw.vertx.extension.DataInconsistentException
import id.soetfw.vertx.extension.ErrorHandler
import id.soetfw.vertx.extension.InvalidCredentials
import id.soetfw.vertx.extension.NotAllowedException
import id.soetfw.vertx.extension.NotFoundException
import id.soetfw.vertx.extension.NullObjectException
import id.soetfw.vertx.extension.RegistrationException
import id.soetfw.vertx.extension.UnauthorizedException
import id.soetfw.vertx.extension.ValidationException
import id.soetfw.vertx.extension.endWithJson
import id.soetfw.vertx.extension.header
import id.soetfw.vertx.extension.logger
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.ext.web.RoutingContext
import java.io.FileNotFoundException

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

object DefaultErrorHandler : ErrorHandler {
    private val log = logger(DefaultErrorHandler::class)
    override fun invoke(context: RoutingContext, e: Throwable) {
        val code = when (e) {
            is FileNotFoundException -> HttpResponseStatus.NOT_FOUND.code()
            is NullObjectException -> HttpResponseStatus.NOT_FOUND.code()
            is DataInconsistentException -> HttpResponseStatus.INTERNAL_SERVER_ERROR.code()
            is NotAllowedException -> HttpResponseStatus.METHOD_NOT_ALLOWED.code()
            is SecurityException -> HttpResponseStatus.UNAUTHORIZED.code()
            is RegistrationException -> HttpResponseStatus.BAD_REQUEST.code()
            is ValidationException -> HttpResponseStatus.BAD_REQUEST.code()
            is BadRequestException -> HttpResponseStatus.BAD_REQUEST.code()
            is UnauthorizedException -> HttpResponseStatus.UNAUTHORIZED.code()
            is NotFoundException -> HttpResponseStatus.NOT_FOUND.code()
            is InvalidCredentials -> HttpResponseStatus.FORBIDDEN.code()
            else ->
                if (context.statusCode() > 0) {
                    context.statusCode()
                } else {
                    500
                }
        }

        if (code.toString().startsWith("5")) {
            log.error(e.message)
        }

        val acceptHeader = context.header("Accept") ?: ""
        val contentTypeHeader = context.header("Content-Type") ?: ""
        if (acceptHeader.contains("/json") || contentTypeHeader.contains("/json")) {
            val result = if (e is ValidationException) {
                mapOf(
                        "message" to "Telah terjadi kesalahan. Silahkan periksa kembali data Anda.",
                        "errors" to e.errors
                )
            } else {
                mapOf(
                        "message" to (e.message ?: ""),
                        "errors" to (e.message ?: "")
                )
            }
            context.response().setStatusCode(code).endWithJson(result)
        } else {
            context.response().setStatusCode(code).end(e.message ?: "")
        }
    }
}