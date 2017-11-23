package id.soetfw.vertx.db.ebean

import id.soetfw.vertx.db.Model
import id.soetfw.vertx.extension.transaction
import io.ebean.EbeanServer
import io.ebean.Query
import io.ebean.Transaction
import kotlin.reflect.KClass

/**
 * [Documentation Here]
 */

abstract class EbeanRepository<T : Model, in I : Any>(protected open val ebean: EbeanServer,
                                                      private val clazz: KClass<T>) {
    protected val query: Query<T>
        get() = ebean.createQuery(clazz.java)

    suspend open fun exists(id: I): Boolean {
        return findOne(id) != null
    }

    suspend open fun findOne(id: I): T? {
        return ebean.find(clazz.java, id)
    }

    suspend open fun findAll(): List<T> {
        return ebean.find(clazz.java).findList()
    }

    suspend open fun count(): Int {
        return query.findCount()
    }

    suspend open fun deleteQuery(deleteQuery: Query<T>, transaction: Transaction? = null): Int {
        return ebean.delete(deleteQuery, transaction)
    }

    suspend open fun delete(o: T, transaction: Transaction? = null): Boolean {
        return ebean.delete(o, transaction)
    }

    suspend open fun deleteAll(transaction: Transaction? = null): Int {
        return ebean.deleteAll(findAll(), transaction)
    }

    suspend open fun deleteById(id: I, transaction: Transaction? = null): Int {
        return ebean.delete(clazz.java, id, transaction)
    }

    suspend open fun saveAll(list: List<T>, transaction: Transaction? = null): Int {
        return ebean.saveAll(list, transaction)
    }

    suspend open fun save(o: T, transaction: Transaction? = null) {
        return ebean.save(o, transaction)
    }

    suspend open fun update(code: I, o: T, transaction: Transaction? = null): Boolean {
        return if (exists(code)) {
            ebean.update(o, transaction)
            true
        } else {
            false
        }
    }

    suspend open fun overwrite(list: List<T>, transaction: Transaction? = null): Int {
        return ebean.transaction(transaction) {
            deleteAll(it) + saveAll(list, it)
        }
    }

}