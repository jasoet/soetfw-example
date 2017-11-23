package com.jojonomic.simple

import com.jojonomic.simple.module.DaggerAppComponent
import id.soetfw.vertx.extension.buildVertx
import id.soetfw.vertx.extension.jsonConfig
import id.soetfw.vertx.extension.retrieveConfig
import id.soetfw.vertx.module.EBeanModule
import id.soetfw.vertx.module.EnvModule
import id.soetfw.vertx.module.VertxModule
import kotlinx.coroutines.experimental.runBlocking

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

object Application {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking<Unit> {

        val vertx = buildVertx()
        val config = vertx.retrieveConfig(jsonConfig("application-config.json"))
        val app = DaggerAppComponent.builder()
                .envModule(EnvModule(config))
                .vertxModule(VertxModule(vertx))
                .eBeanModule(EBeanModule())
                .build()

        val mainVerticle = app.mainVerticle()
        vertx.deployVerticle(mainVerticle)
    }

}