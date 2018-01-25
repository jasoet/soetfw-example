package com.jojonomic.simple.db.repository

import com.jojonomic.simple.db.model.Item
import id.yoframework.ebean.extension.invoke
import id.yoframework.ebean.repository.Repository
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
        Repository<Item, Int>(ebean, Item::class) {

    fun findByName(name: String): List<Item> {
        return query {
            where()
                    .ilike("name", "%$name%")
        }.findList()
    }

    fun findByPriceMin(price: Double): List<Item> {
        return query {
            where()
                    .gt("price", price)
        }.findList()
    }
}