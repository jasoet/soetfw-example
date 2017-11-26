package com.jojonomic.simple

import com.jojonomic.simple.module.DaggerAppComponent
import id.soetfw.vertx.extension.buildVertx
import id.soetfw.vertx.extension.deployVerticle
import id.soetfw.vertx.extension.jsonConfig
import id.soetfw.vertx.extension.logger
import id.soetfw.vertx.extension.retrieveConfig
import id.soetfw.vertx.extension.useLogback
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
        useLogback()

        val log = logger<Application>()
        try {
            log.info("Start build Vertx")
            val vertx = buildVertx()
            val config = vertx.retrieveConfig(jsonConfig("application-config.json"))
            log.info("Start initialize components")
            val app = DaggerAppComponent.builder()
                    .envModule(EnvModule(config))
                    .vertxModule(VertxModule(vertx))
                    .eBeanModule(EBeanModule())
                    .build()


            log.info("Start deploy Verticle")
            val mainVerticle = app.mainVerticle()
            vertx.deployVerticle(mainVerticle, config)

            log.info("Verticle Deployed")
        } catch (e: Exception) {
            log.error("Failed to Start Application", e)
            System.exit(1)
        }

    }

}