package id.soetfw.vertx.extension

import io.vertx.core.logging.SLF4JLogDelegateFactory
import net.logstash.logback.marker.Markers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import io.vertx.core.logging.LoggerFactory as VertxLoggerFactory

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */
inline fun <reified T : Any> logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}

fun logger(clz: KClass<*>): Logger {
    return LoggerFactory.getLogger(clz.qualifiedName)
}

fun logger(name: String): Logger {
    return LoggerFactory.getLogger(name)
}

fun useLogback() {
    System.setProperty(VertxLoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME, clazz<SLF4JLogDelegateFactory>().name)
}

sealed class LogType

class INFO(val message: String, val throwable: Throwable? = null) : LogType()
class WARN(val message: String, val throwable: Throwable? = null) : LogType()
class ERROR(val message: String, val throwable: Throwable? = null) : LogType()

fun Logger.log(log: LogType, defaultParam: Map<String, Any> = emptyMap(), vararg params: Pair<String, Any>) {
    val allParams = defaultParam + params.toList().toMap()
    when (log) {
        is INFO -> {
            if (log.throwable != null) {
                this.info(Markers.appendEntries(allParams), log.message, log.throwable)
            } else {
                this.info(Markers.appendEntries(allParams), log.message)
            }
        }
        is WARN -> {
            if (log.throwable != null) {
                this.warn(Markers.appendEntries(allParams), log.message, log.throwable)
            } else {
                this.warn(Markers.appendEntries(allParams), log.message)
            }
        }
        is ERROR -> {
            if (log.throwable != null) {
                this.error(Markers.appendEntries(allParams), log.message, log.throwable)
            } else {
                this.error(Markers.appendEntries(allParams), log.message)
            }
        }
    }
}


