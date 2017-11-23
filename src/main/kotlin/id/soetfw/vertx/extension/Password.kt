package id.soetfw.vertx.extension

import org.mindrot.jbcrypt.BCrypt

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

typealias PlainPassword = String
typealias Password = String

fun PlainPassword.encode(): Password {
    return BCrypt.hashpw(this, BCrypt.gensalt())
}

fun PlainPassword.match(password: Password): Boolean {
    return BCrypt.checkpw(this, password)
}