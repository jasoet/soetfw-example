package id.soetfw.vertx.security

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User
import kotlinx.coroutines.experimental.runBlocking

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

interface SecurityProvider : AuthProvider {
    override fun authenticate(authInfo: JsonObject, resultHandler: Handler<AsyncResult<User>>) = runBlocking {
        try {
            val user = authenticate(authInfo)
            resultHandler.handle(Future.succeededFuture(user))
        } catch (e: Exception) {
            val exception = SecurityException(e)
            resultHandler.handle(Future.failedFuture(exception))
        }
    }

    suspend fun authenticate(authInfo: JsonObject): SecurityUser
}