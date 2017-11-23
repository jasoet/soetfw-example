package id.soetfw.vertx.extension

import io.vertx.core.buffer.Buffer
import io.vertx.core.file.FileSystem
import io.vertx.kotlin.coroutines.awaitResult

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

suspend fun FileSystem.readFile(path: String): Buffer {
    return awaitResult { this.readFile(path, it) }
}

suspend fun FileSystem.writeFile(path: String, data: Buffer): Void {
    return awaitResult { this.writeFile(path, data, it) }
}
