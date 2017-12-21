package com.jojonomic.simple.module

import com.jojonomic.simple.verticle.MainVerticle
import dagger.Component
import id.yoframework.core.module.CoreModule
import id.yoframework.ebean.module.EBeanModule
import id.yoframework.web.module.WebModule
import io.ebean.EbeanServer
import javax.inject.Singleton
import javax.sql.DataSource

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Singleton
@Component(modules = [CoreModule::class, EBeanModule::class, WebModule::class])
interface AppComponent {
    fun mainVerticle(): MainVerticle
    fun ebean(): EbeanServer
    fun dataSource(): DataSource
}