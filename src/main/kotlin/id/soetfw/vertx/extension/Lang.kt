package id.soetfw.vertx.extension

import org.apache.commons.lang3.RandomStringUtils


/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

internal inline fun <reified T : Any> clazz() = T::class.java

fun randomAlpha(count: Int): String {
    return RandomStringUtils.randomAlphanumeric(count)
}

inline fun <T : Any?> measureTime(block: () -> T): Pair<T, Long> {
    val start = System.currentTimeMillis()
    val result = block()
    return result to (System.currentTimeMillis() - start)
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun <T> T?.orNotFound(message: String): T {
    return this ?: throw  NullObjectException(message)
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun <T> T?.orBadRequest(message: String): T {
    return if (this == null) {
        throw  BadRequestException(message)
    } else {
        this
    }
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun <T> T?.orUnauthorized(message: String): T {
    return this ?: throw UnauthorizedException(message)
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun <T> T?.orForbidden(message: String): T {
    return this ?: throw InvalidCredentials(message)
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun <T> T?.orDataError(message: String): T {
    return this ?: throw DataInconsistentException(message)
}

@Suppress("NOTHING_TO_INLINE")
inline fun String.slugify(): String {
    return this.toLowerCase()
            .replace("[^a-z0-9]".toRegex(), " ")
            .replace("\\s+".toRegex(), " ")
            .replace(" ", "-")
}

inline fun <T> executeTimeMillis(block: () -> T): Pair<T, Long> {
    val start = System.currentTimeMillis()
    val result = block()
    val timeSpent = System.currentTimeMillis() - start
    return result to timeSpent
}

data class NullObjectException(override val message: String? = null, val ex: Throwable? = null) : RuntimeException(message, ex)
data class DataInconsistentException(override val message: String? = null, val ex: Throwable? = null) : RuntimeException(message, ex)
data class NotAllowedException(override val message: String? = null, val ex: Throwable? = null) : RuntimeException(message, ex)
data class RegistrationException(override val message: String? = null, val ex: Throwable? = null) : RuntimeException(message, ex)
data class BadRequestException(override val message: String? = null, val ex: Throwable? = null) : RuntimeException(message, ex)
data class UnauthorizedException(override val message: String? = null, val ex: Throwable? = null) : RuntimeException(message, ex)
data class NotFoundException(override val message: String? = null, val ex: Throwable? = null) : RuntimeException(message, ex)
data class ValidationException(val errors: List<String>, val ex: Exception? = null) : RuntimeException(ex)
data class InvalidCredentials(override val message: String? = null, val ex: Throwable? = null) : RuntimeException(message, ex)
