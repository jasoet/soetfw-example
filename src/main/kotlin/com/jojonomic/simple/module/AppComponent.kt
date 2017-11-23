package com.jojonomic.simple.module

import com.jojonomic.simple.verticle.MainVerticle
import dagger.Component
import id.soetfw.vertx.module.EBeanModule
import id.soetfw.vertx.module.EnvModule
import id.soetfw.vertx.module.VertxModule
import io.ebean.EbeanServer
import javax.inject.Singleton
import javax.sql.DataSource

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Singleton
@Component(modules = arrayOf(EnvModule::class, VertxModule::class, EBeanModule::class))
interface AppComponent {
    fun mainVerticle(): MainVerticle
    fun ebean(): EbeanServer
    fun dataSource(): DataSource
}