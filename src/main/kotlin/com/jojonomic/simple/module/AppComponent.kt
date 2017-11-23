package com.jojonomic.simple.module

import com.jojonomic.simple.verticle.MainVerticle
import dagger.Component
import id.soetfw.vertx.module.EBeanModule
import id.soetfw.vertx.module.EnvModule
import id.soetfw.vertx.module.VertxModule
import io.ebean.EbeanServer
import io.vertx.core.json.JsonObject
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Singleton
@Component(modules = [EnvModule::class, VertxModule::class, EBeanModule::class])
interface AppComponent {
    fun mainVerticle(): MainVerticle
}