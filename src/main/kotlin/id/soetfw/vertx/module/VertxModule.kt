package id.soetfw.vertx.module

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import dagger.Module
import dagger.Provides
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.file.FileSystem
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.templ.PebbleTemplateEngine
import io.vertx.ext.web.templ.TemplateEngine
import javax.inject.Singleton
import javax.validation.Validation
import javax.validation.Validator

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Module
class VertxModule(val vertx: Vertx) {

    init {
        Json.mapper.apply {
            registerKotlinModule()
            registerModule(ParameterNamesModule())
            registerModule(JavaTimeModule())
        }

        Json.prettyMapper.apply {
            registerKotlinModule()
            registerModule(ParameterNamesModule())
            registerModule(JavaTimeModule())
        }
    }

    @Provides
    fun provideRouter(): Router {
        return Router.router(vertx)
    }

    @Singleton
    @Provides
    fun provideEventBus(): EventBus {
        return vertx.eventBus()
    }

    @Provides
    @Singleton
    fun provideVertx(): Vertx {
        return vertx
    }

    @Provides
    @Singleton
    fun provideObjectMapper(): ObjectMapper {
        return Json.mapper
    }

    @Provides
    @Singleton
    fun provideFileSystem(): FileSystem {
        return vertx.fileSystem()
    }

    @Provides
    @Singleton
    fun provideValidator(): Validator {
        val validatorFactory = Validation.buildDefaultValidatorFactory()
        return validatorFactory.validator
    }

    @Provides
    @Singleton
    fun provideWebClient(): WebClient {
        return WebClient.create(vertx)
    }

    @Provides
    @Singleton
    fun providePebbleTemplate(): TemplateEngine {
        return PebbleTemplateEngine.create(vertx)
    }

}