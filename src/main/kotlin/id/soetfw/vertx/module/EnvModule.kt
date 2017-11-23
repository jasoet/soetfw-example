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

    @Provides
    @Singleton
    @Named("imageLocation")
    fun imageLocation(): String {
        return try {
            val imageLocation = config.getStringExcept("IMAGE_LOCATION", "IMAGE_LOCATION config is required")
            log.info("Initialize IMAGE_LOCATION with value $imageLocation")
            imageLocation
        } catch (e: Exception) {
            log.error("Initialize IMAGE_LOCATION failed ${e.message}", e)
            throw e
        }
    }

    @Provides
    @Singleton
    @Named("docLocation")
    fun docLocation(): String {
        return try {
            val documentLocation = config.getStringExcept("DOCUMENT_LOCATION", "DOCUMENT_LOCATION config is required")
            log.info("Initialize DOCUMENT_LOCATION with value $documentLocation")
            documentLocation
        } catch (e: Exception) {
            log.error("Initialize DOCUMENT_LOCATION failed ${e.message}", e)
            throw e
        }
    }

    @Provides
    @Singleton
    @Named("feLandingBaseUrl")
    fun feLandingBaseUrl(): String {
        return try {
            val url = config.getStringExcept("FE_LANDING_BASE_URL", "FE_LANDING_BASE_URL config is required")
            log.info("Initialize FE_LANDING_BASE_URL with value $url")
            if (url.endsWith("/")) {
                url
            } else {
                "$url/"
            }
        } catch (e: Exception) {
            log.error("Initialize FE_LANDING_BASE_URL failed ${e.message}", e)
            throw e
        }
    }

    @Provides
    @Singleton
    @Named("feMemberBaseUrl")
    fun feMemberBaseUrl(): String {
        return try {
            val url = config.getStringExcept("FE_MEMBER_BASE_URL", "FE_MEMBER_BASE_URL config is required")
            log.info("Initialize FE_MEMBER_BASE_URL with value $url")
            if (url.endsWith("/")) {
                url
            } else {
                "$url/"
            }
        } catch (e: Exception) {
            log.error("Initialize FE_MEMBER_BASE_URL failed ${e.message}", e)
            throw e
        }
    }

    @Provides
    @Singleton
    @Named("feAdminBaseUrl")
    fun feAdminBaseUrl(): String {
        return try {
            val url = config.getStringExcept("FE_ADMIN_BASE_URL", "FE_ADMIN_BASE_URL config is required")
            log.info("Initialize FE_ADMIN_BASE_URL with value $url")
            if (url.endsWith("/")) {
                url
            } else {
                "$url/"
            }
        } catch (e: Exception) {
            log.error("Initialize FE_ADMIN_BASE_URL failed ${e.message}", e)
            throw e
        }
    }

    @Provides
    @Singleton
    @Named("apiBaseUrl")
    fun apiBaseUrl(): String {
        return try {
            val url = config.getStringExcept("API_BASE_URL", "API_BASE_URL config is required")
            log.info("Initialize API_BASE_URL with value $url")
            if (url.endsWith("/")) {
                url
            } else {
                "$url/"
            }
        } catch (e: Exception) {
            log.error("Initialize API_BASE_URL failed ${e.message}", e)
            throw e
        }
    }

}
