package id.soetfw.vertx.extension

import io.ebean.EbeanServer
import io.ebean.ExpressionList
import io.ebean.Platform
import io.ebean.Query
import io.ebean.Transaction
import io.ebean.config.DbMigrationConfig
import io.ebean.config.ServerConfig
import io.ebean.dbmigration.DbMigration
import org.flywaydb.core.Flyway
import javax.sql.DataSource

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

inline fun <R : Any> EbeanServer.transaction(existingTransaction: Transaction? = null, operation: (Transaction) -> R): R {
    return if (existingTransaction != null) {
        operation(existingTransaction)
    } else {
        val transaction = this.beginTransaction()
        try {
            val result = operation(transaction)
            this.commitTransaction()
            result
        } catch (e: Exception) {
            this.rollbackTransaction()
            throw e
        } finally {
            this.endTransaction()
        }
    }
}

fun DataSource.assertConnectionOpen() {
    if (this.connection.isClosed) {
        throw AssertionError("Connection is Closed!")
    }
}

operator fun <T : Any> Query<T>.invoke(expression: Query<T>.() -> ExpressionList<T>): Query<T> {
    return expression(this).query()
}

fun DataSource.executeMigration() {
    val dataSource = this
    val flyway = Flyway().apply {
        setDataSource(dataSource)
        setLocations("classpath:/dbmigration/mysql")
    }
    flyway.migrate()
}

fun EbeanServer.generateMigrationFile(platform: Platform, prefix: String): String? {
    val ebean = this
    val serverConfig = ServerConfig().apply {
        this.migrationConfig = DbMigrationConfig().apply {
            applyPrefix = "V"
        }
    }
    val dbMigration = DbMigration().apply {
        setServerConfig(serverConfig)
        setServer(ebean)
        addPlatform(platform, prefix)
    }
    return dbMigration.generateMigration()
}
