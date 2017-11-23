package id.soetfw.vertx.controller

import io.vertx.ext.web.Router

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

abstract class Controller(private val handlers: Router.() -> Unit) {
    abstract val router: Router
    fun create(): Router {
        return router.apply {
            handlers()
        }
    }
}