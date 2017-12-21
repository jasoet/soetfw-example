package com.jojonomic.simple.verticle

import com.jojonomic.simple.controller.MainController
import id.yoframework.core.extension.logger.logger
import id.yoframework.web.extension.startHttpServer
import io.vertx.kotlin.coroutines.CoroutineVerticle
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Singleton
class MainVerticle @Inject constructor(private val mainController: MainController) : CoroutineVerticle() {
    private val log = logger(MainVerticle::class)

    suspend override fun start() {
        val router = mainController.create()
        try {
            val port = config.getInteger("HTTP_PORT")
            val httpServer = vertx.startHttpServer(router, port)
            log.info("Server Started at port ${httpServer.actualPort()}")
        } catch (e: Exception) {
            log.error("Server failed to Start caused by ${e.message}", e)
        }
    }
}