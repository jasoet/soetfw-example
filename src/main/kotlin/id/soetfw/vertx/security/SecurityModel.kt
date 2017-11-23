package id.soetfw.vertx.security

import id.soetfw.vertx.db.Model
import io.vertx.core.json.JsonObject

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

interface SecurityModel : Model {
    fun permission(): Set<String>
    fun toJsonObject(): JsonObject
}