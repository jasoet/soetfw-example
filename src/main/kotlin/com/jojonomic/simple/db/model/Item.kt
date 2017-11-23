package com.jojonomic.simple.db.model

import id.soetfw.vertx.db.Model
import io.ebean.annotation.Index
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
        var description: String) : Model
