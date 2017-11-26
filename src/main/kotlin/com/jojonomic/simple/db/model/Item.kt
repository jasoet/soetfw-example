package com.jojonomic.simple.db.model

import id.soetfw.vertx.db.Model
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Entity
@Table(name = "item")
data class Item(
        @Id
        var id: Int? = null,
        var name: String,
        var price: Double,
        val title: String? = "",
        var description: String) : Model
