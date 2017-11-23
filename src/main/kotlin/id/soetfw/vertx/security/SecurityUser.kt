package id.soetfw.vertx.security

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AbstractUser
import io.vertx.ext.auth.AuthProvider
import java.io.Serializable

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

class SecurityUser(private val model: SecurityModel, private val accesses: List<String>) : AbstractUser(), Serializable {
    override fun doIsPermitted(permission: String, resultHandler: Handler<AsyncResult<Boolean>>) {
        resultHandler.handle(Future.succeededFuture(doIsPermitted(permission)))
    }

    fun doIsPermitted(permission: String): Boolean {
        return accesses.contains(permission)
    }

    override fun setAuthProvider(authProvider: AuthProvider?) {}

    override fun principal(): JsonObject {
        return model.toJsonObject()
    }
}