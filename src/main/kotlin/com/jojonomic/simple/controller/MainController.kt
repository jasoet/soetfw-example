package com.jojonomic.simple.controller

import com.jojonomic.simple.handler.ItemHandler
import id.yoframework.web.controller.Controller
import id.yoframework.web.exception.orNotFound
import id.yoframework.web.extension.jsonHandler
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Singleton
class MainController @Inject constructor(override val router: Router,
                                         private val itemHandler: ItemHandler) : Controller({

    route().handler(BodyHandler.create())
    get("/search/:name/:price").jsonHandler {
        val name = pathParam("name") orNotFound "Name not Found"
        val price = pathParam("price").toDoubleOrNull() orNotFound "Price Not Found"
        itemHandler.getByNameAndPriceMin(name, price)
    }

    get("/:id").jsonHandler {
        val id = pathParam("id").toIntOrNull() orNotFound "Item not Found"
        itemHandler.getItem(id) orNotFound "Item not found!"
    }

    get("/").jsonHandler {
        itemHandler.getAll()
    }

    post("/").jsonHandler { itemHandler.insert(this) }

})
