package com.jojonomic.simple.controller

import com.jojonomic.simple.plain.User
import id.soetfw.vertx.controller.Controller
import id.soetfw.vertx.extension.jsonHandler
import io.vertx.ext.web.Router
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Singleton
class MainController @Inject constructor(override val router: Router) : Controller({
    get("/").jsonHandler {
        User("Deny Prasetyo", "Jogja", 17)
    }
})
