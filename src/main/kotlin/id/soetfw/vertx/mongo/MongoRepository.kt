package id.soetfw.vertx.mongo

import com.mongodb.WriteResult
import id.soetfw.vertx.extension.invoke
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Key
import org.mongodb.morphia.aggregation.AggregationPipeline
import org.mongodb.morphia.query.FindOptions
import org.mongodb.morphia.query.Query
import org.mongodb.morphia.query.UpdateOperations
import org.mongodb.morphia.query.UpdateResults
import kotlin.reflect.KClass

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo
 */

abstract class MongoRepository<T : Model, in ID : Any>(private val datastore: Datastore,
                                                       private val idField: String,
                                                       private val entityClass: KClass<T>) {

    val query: Query<T>
        get() {
            return datastore.createQuery(entityClass.java)
        }

    val updateOps: UpdateOperations<T>
        get() {
            return datastore.createUpdateOperations(entityClass.java)
        }

    val aggregate: AggregationPipeline
        get() {
            return datastore.createAggregation(entityClass.java)
        }

    abstract fun defaultUpdateOps(model: T): UpdateOperations<T>

    suspend open fun save(model: T): Key<T> {
        return datastore.save(model)
    }

    suspend open fun save(iterableModel: Iterable<T>): Iterable<Key<T>> {
        return datastore.save(iterableModel)
    }

    suspend open fun findAll(orderBy: String = "-createdOn", findOptions: FindOptions = FindOptions()): List<T> {
        return datastore.createQuery(entityClass.java).order(orderBy).asList(findOptions)
    }

    suspend open fun findByKey(id: Key<T>): T? {
        return datastore.getByKey(entityClass.java, id)
    }

    suspend open fun findByKeys(id: Iterable<Key<T>>): List<T> {
        return datastore.getByKeys(entityClass.java, id)
    }

    suspend open fun findByIdQuery(id: ID): Query<T> {
        return datastore.createQuery(entityClass.java).field(idField).equal(id)
    }

    suspend open fun findById(id: ID): T? {
        return findByIdQuery(id).get()
    }

    suspend open fun findByIdsQuery(ids: Iterable<*>): Query<T> {
        return datastore.createQuery(entityClass.java).field(idField).`in`(ids)
    }

    suspend open fun findByIds(ids: Iterable<*>): T? {
        return findByIdsQuery(ids).get()
    }

    suspend open fun findByFieldsQuery(vararg fieldList: Pair<String, Any?>, findOptions: FindOptions = FindOptions()): Query<T> {
        return fieldList.fold(query) { q, (field, value) ->
            q.filter(field, value)
        }
    }

    suspend open fun findByFields(vararg fieldList: Pair<String, Any?>, findOptions: FindOptions = FindOptions()): T? {
        return findByFieldsQuery(*fieldList, findOptions = findOptions).get(findOptions)
    }

    suspend open fun findByFields(filterQuery: Query<T>, findOptions: FindOptions = FindOptions()): T? {
        return filterQuery.get(findOptions)
    }

    suspend open fun findAllByFields(vararg fieldList: Pair<String, Any?>, orderBy: String = "-createdOn", findOptions: FindOptions = FindOptions()): List<T> {
        return findByFieldsQuery(fieldList = *fieldList, findOptions = findOptions).order(orderBy).asList(findOptions)
    }

    suspend open fun findAllByFields(filterQuery: Query<T>, orderBy: String = "-createdOn", findOptions: FindOptions = FindOptions()): List<T> {
        return filterQuery.order(orderBy).asList(findOptions)
    }

    suspend open fun countByFields(vararg fieldList: Pair<String, Any?>): Long {
        return findByFieldsQuery(fieldList = *fieldList).count()
    }

    suspend open fun countByFields(filterQuery: Query<T>): Long {
        return filterQuery.count()
    }

    suspend open fun countTotalData(listData: List<T>): Long {
        return listData.count().toLong()
    }

    suspend open fun update(id: ID, updateOperation: UpdateOperations<T>): UpdateResults {
        val query = datastore.createQuery(entityClass.java).field(idField).equal(id)
        return datastore.update(query, updateOperation)
    }

    suspend open fun update(query: Query<T>, updateOperation: UpdateOperations<T>): UpdateResults {
        return datastore.update(query, updateOperation)
    }

    suspend open fun update(id: ID, model: T): UpdateResults {
        val idQuery = query {
            field(idField).equal(id)
        }
        return update(idQuery, defaultUpdateOps(model))
    }

    suspend open fun updateByMap(vararg updateQuery: Pair<String, Any?>, updateOperation: Map<String, Any?>): UpdateResults {
        return updateByFields(*updateQuery, updateOperation = updateOperation.toList())
    }

    suspend open fun updateByFields(vararg updateQuery: Pair<String, Any?>, updateOperation: List<Pair<String, Any?>>): UpdateResults {
        val queries = updateQuery.fold(query) { q, (field, value) ->
            q.filter(field, value)
        }

        val updateOperations = updateOperation.toList().fold(updateOps) { u, (field, value) ->
            u(field to value)
        }

        return update(queries, updateOperations)
    }

    suspend open fun delete(model: T): WriteResult {
        return datastore.delete(model)
    }

    suspend open fun deleteById(id: ID): WriteResult {
        return datastore.delete(entityClass.java, id)
    }

    suspend open fun deleteByIds(ids: Iterable<ID>): WriteResult {
        return datastore.delete(entityClass.java, ids)
    }

    suspend open fun deleteByFields(vararg deleteQuery: Pair<String, Any?>): WriteResult {
        val queries = deleteQuery.fold(query) { q, (field, value) ->
            q.filter(field, value)
        }
        return datastore.delete(queries)
    }

}