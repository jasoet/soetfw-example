package id.soetfw.vertx.module

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dagger.Module
import dagger.Provides
import id.soetfw.vertx.extension.getStringExcept
import io.ebean.EbeanServer
import io.ebean.EbeanServerFactory
import io.ebean.config.ServerConfig
import io.vertx.core.json.JsonObject
import javax.inject.Named
import javax.inject.Singleton
import javax.sql.DataSource


/**
 * [Documentation Here]
 */

@Module(includes = [EnvModule::class])
class EBeanModule {

    @Provides
    @Singleton
    @Named("dbUser")
    fun username(config: JsonObject): String {
        val key = "DATABASE_USER"
        return config.getStringExcept(key, "$key Required")
    }

    @Provides
    @Singleton
    @Named("dbPassword")
    fun password(config: JsonObject): String {
        val key = "DATABASE_PASSWORD"
        return config.getStringExcept(key, "$key Required")
    }

    @Provides
    @Singleton
    @Named("dbUrl")
    fun url(config: JsonObject): String {
        val key = "DATABASE_URL"
        return config.getStringExcept(key, "$key Required")
    }

    @Provides
    @Singleton
    @Named("dbDriver")
    fun driver(config: JsonObject): String {
        val key = "DATABASE_DRIVER_CLASSNAME"
        return config.getStringExcept(key, "$key Required")
    }

    @Provides
    @Singleton
    @Named("dataSource")
    fun dataSource(@Named("dbUser") user: String,
                   @Named("dbPassword") password: String,
                   @Named("dbUrl") url: String,
                   @Named("dbDriver") driver: String): DataSource {

        val config = HikariConfig()
        config.poolName = "HikariPool"
        config.jdbcUrl = url
        config.username = user
        config.password = password
        config.driverClassName = driver

        /**
         * We will research for best configuration
        config.connectionTimeout = connectionTimeout
        config.minimumIdle = minimumIdle
        config.idleTimeout = idleTimeout
        config.maximumPoolSize = maximumPoolSize
        config.maxLifetime = idleTimeout
         **/

        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

        return HikariDataSource(config)
    }

    @Singleton
    @Provides
    fun ebeanServer(@Named("dataSource") dataSource: DataSource): EbeanServer {
        val config = ServerConfig().apply {
            name = "mysql"
            setDataSource(dataSource)
        }

        return EbeanServerFactory.create(config)
    }

}