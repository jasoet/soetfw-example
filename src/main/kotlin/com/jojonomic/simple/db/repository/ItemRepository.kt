package com.jojonomic.simple.db.repository

import com.jojonomic.simple.db.model.Item
import id.soetfw.vertx.db.ebean.EbeanRepository
import id.soetfw.vertx.extension.invoke
import io.ebean.EbeanServer
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Singleton
class ItemRepository @Inject constructor(override val ebean: EbeanServer) :
        EbeanRepository<Item, Int>(ebean, Item::class) {

    suspend fun findByName(name: String): List<Item> {
        return query {
            where()
                    .ilike("name", "%$name%")
        }.findList()
    }

    suspend fun findByPriceMin(price: Double): List<Item> {
        return query {
            where()
                    .gt("price", price)
        }.findList()
    }
}