package id.soetfw.vertx.security

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

data class SecurityException(override val message: String? = null, val ex: Throwable? = null) : RuntimeException(message, ex)

data class InvalidCredentials(override val message: String? = null, val ex: Throwable? = null) : RuntimeException(message, ex)
