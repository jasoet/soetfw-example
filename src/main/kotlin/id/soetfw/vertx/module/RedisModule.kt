package id.soetfw.vertx.module

import dagger.Module
import dagger.Provides
import id.soetfw.vertx.extension.getExcept
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.redis.RedisClient
import io.vertx.redis.RedisOptions
import javax.inject.Named
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Module(includes = arrayOf(EnvModule::class, VertxModule::class))
class RedisModule {
    companion object {
        fun redisOption(host: String = "localhost", port: Int = 6379, address: String = "io.vertx.redis"): RedisOptions {
            return RedisOptions().apply {
                setHost(host)
                setPort(port)
                setAddress(address)
            }
        }
    }

    @Provides
    @Singleton
    @Named("eventBusRedisConfig")
    fun redisConfig(config: JsonObject): RedisOptions {
        val redisHost: String = config.getExcept("REDIS_HOST", { "$it is Required!" })
        val redisPort = config.getInteger("REDIS_PORT", 6379)
        val redisAddress = config.getString("REDIS_ADDRESS", "io.vertx.redis")
        return redisOption(redisHost, redisPort, redisAddress)
    }


    @Provides
    @Singleton
    @Named("eventBusRedisClient")
    fun redisClient(@Named("eventBusRedisConfig") option: RedisOptions, vertx: Vertx): RedisClient {
        return RedisClient.create(vertx, option)
    }

}