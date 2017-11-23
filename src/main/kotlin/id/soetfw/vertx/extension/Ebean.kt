package id.soetfw.vertx.extension

import io.ebean.EbeanServer
import io.ebean.ExpressionList
import io.ebean.Query
import io.ebean.Transaction
import io.ebean.annotation.Platform
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

fun executeMigration(dataSource: DataSource) {
    val flyway = Flyway().apply {
        setDataSource(dataSource)
    }
    flyway.migrate()
}

fun generateMigration(ebean: EbeanServer) {
    val dbMigration = DbMigration.create().apply {
        setServer(ebean)
        addPlatform(Platform.POSTGRES, "PostgreSQL")
    }
    dbMigration.generateMigration()
}
