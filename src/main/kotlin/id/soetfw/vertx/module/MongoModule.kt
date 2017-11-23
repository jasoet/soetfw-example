package id.soetfw.vertx.module

import com.mongodb.MongoClient
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoDatabase
import dagger.Module
import dagger.Provides
import id.soetfw.vertx.extension.getStringExcept
import id.soetfw.vertx.extension.logger
import io.vertx.core.json.JsonObject
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.experimental.CoroutineContext

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Module(includes = arrayOf(EnvModule::class))
class MongoModule {
    private val log = logger<MongoModule>()

    @Provides
    @Singleton
    @Named("databaseName")
    fun databaseName(config: JsonObject): String {
        val key = "MONGODB_DATABASE"
        return config.getStringExcept(key, "$key is Required!")
    }

    @Provides
    @Singleton
    fun provideMongoClient(config: JsonObject): MongoClient {
        val host = config.getString("MONGODB_HOST")
        val port = config.getInteger("MONGODB_PORT")
        val server = ServerAddress(host, port)

        val mongoUsername = config.getString("MONGODB_USERNAME", "")
        val mongoPassword = config.getString("MONGODB_PASSWORD", "")

        val databaseName = config.getString("MONGODB_DATABASE")

        val client = if (mongoUsername.isBlank() && mongoPassword.isBlank()) {
            log.info("Initialize MongoClient with $host:$port without auth")
            MongoClient(server)
        } else {
            val credentials = MongoCredential.createScramSha1Credential(
                    mongoUsername, databaseName, mongoPassword.toCharArray())
            val credentialsList = listOf(credentials)
            log.info("Initialize MongoClient with $host:$port")
            MongoClient(server, credentialsList)
        }

        val address = try {
            log.info("Trying to Connect MongoDB Database [$host:$port]...")
            client.address
        } catch (e: Exception) {
            log.error("Mongo is not Connected [${e.message}]", e)
            client.close()
            throw e
        }

        log.info("MongoDB is Connected to $address")
        return client
    }

    @Provides
    @Singleton
    fun provideMongoDatabase(mongoClient: MongoClient, config: JsonObject): MongoDatabase {
        val databaseName = config.getString("MONGODB_DATABASE")
        log.info("Initialize Mongo Database with name $databaseName")
        return mongoClient.getDatabase(databaseName)
    }

    @Provides
    @Singleton
    @Named("mongoThreadPool")
    fun mongoThreadPool(config: JsonObject): CoroutineContext {
        val mongoThreadPoolSize = config.getInteger("MONGODB_THREAD_POOL_SIZE", 6)
        log.info("Initialize Mongo Database with thread pool size $mongoThreadPoolSize")
        return newFixedThreadPoolContext(mongoThreadPoolSize, name = "Mongo Thread Pool")
    }

}