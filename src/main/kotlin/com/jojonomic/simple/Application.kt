package com.jojonomic.simple

import com.jojonomic.simple.module.DaggerAppComponent
import id.yoframework.core.extension.config.jsonConfig
import id.yoframework.core.extension.config.retrieveConfig
import id.yoframework.core.extension.logger.logger
import id.yoframework.core.extension.logger.useLogback
import id.yoframework.core.extension.vertx.buildVertx
import id.yoframework.core.extension.vertx.deployVerticle
import id.yoframework.core.module.CoreModule
import id.yoframework.db.executeMigration
import id.yoframework.ebean.extension.generateMigrationFile
import id.yoframework.ebean.module.EBeanModule
import id.yoframework.web.module.WebModule
import io.ebean.annotation.Platform
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
                .coreModule(CoreModule(config, vertx))
                .webModule(WebModule())
                .eBeanModule(EBeanModule())
                .build()

            val ebean = app.ebean()
            val migration = ebean.generateMigrationFile(Platform.MYSQL, "mysql")

            if (migration != null) {
                log.info("Migration file $migration created!")
            }

            log.info("Start deploy Verticle")
            val mainVerticle = app.mainVerticle()
            vertx.deployVerticle(mainVerticle, config)

            val dataSource = app.dataSource()
            dataSource.executeMigration("classpath:/dbmigration/mysql")

            log.info("Verticle Deployed")
        } catch (e: Exception) {
            log.error("Failed to Start Application", e)
            System.exit(1)
        }

    }

}