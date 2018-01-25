package com.jojonomic.simple.handler

import com.jojonomic.simple.db.model.Item
import com.jojonomic.simple.db.repository.ItemRepository
import id.yoframework.core.extension.json.getExcept
import id.yoframework.core.extension.logger.logger
import id.yoframework.web.exception.orBadRequest
import id.yoframework.web.extension.jsonBody
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import kotlinx.coroutines.experimental.async
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Singleton
class ItemHandler @Inject constructor(private val itemRepository: ItemRepository) {

    private val log = logger<ItemHandler>()
    fun insert(context: RoutingContext): JsonObject {
        val body = context.jsonBody() orBadRequest "Body is Empty"
        val name = body.getExcept<String>("name")
        val price = body.getExcept<Double>("price")
        val description = body.getExcept<String>("description")

        val item = Item(name = name, price = price, description = description)
        itemRepository.save(item)

        return json {
            obj(
                "success" to true,
                "item" to item
            )
        }
    }

    fun getItem(id: Int): Item? {
        return itemRepository.findOne(id)
    }

    fun getAll(): List<Item> {
        return itemRepository.findAll()
    }

    suspend fun getByNameAndPriceMin(name: String, price: Double): Pair<List<Item>, List<Item>> {
        val itemByName = async {
            itemRepository.findByName(name)
        }

        val itemByPriceMin = async {
            itemRepository.findByPriceMin(price)
        }

        return itemByName.await() to itemByPriceMin.await()
    }

}