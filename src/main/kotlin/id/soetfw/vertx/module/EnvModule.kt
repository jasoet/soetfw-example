package id.soetfw.vertx.module

import dagger.Module
import dagger.Provides
import id.soetfw.vertx.extension.getStringExcept
import id.soetfw.vertx.extension.logger
import io.vertx.core.json.JsonObject
import javax.inject.Named
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Module
class EnvModule(val config: JsonObject) {

    private val log = logger(EnvModule::class)

    @Provides
    @Singleton
    fun provideConfig(): JsonObject {
        return config
    }
}
