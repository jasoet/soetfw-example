package id.soetfw.vertx.security

import id.soetfw.vertx.mongo.Model
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